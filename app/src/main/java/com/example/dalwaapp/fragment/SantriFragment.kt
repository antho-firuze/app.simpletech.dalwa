package com.example.dalwaapp.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.dalwaapp.*
import com.example.dalwaapp.auth_activity.ChangePwdActivity
import com.example.dalwaapp.helper.F
import com.example.dalwaapp.model.tListSantri
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_santri.*
import kotlinx.android.synthetic.main.list_santri.view.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.json.JSONObject

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

class SantriFragment : Fragment() {

    private var santri: ArrayList<tListSantri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        if (isRefresh)
            loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_santri, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.opt_add -> {
                context?.startActivity(Intent(view?.context, AddSantriActivity::class.java))
            }
            R.id.opt_logout -> {
                F().actionLogout((activity as AppCompatActivity))
            }
            R.id.opt_chgpwd -> {
                context?.startActivity(Intent(context, ChangePwdActivity::class.java))
            }
            R.id.opt_profile -> {
                context?.startActivity(Intent(context, ProfileActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_santri, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        loadData()
    }

    private fun loadData() {
        if (!session!!.isLogin)
            return

        val progressDialog = indeterminateProgressDialog(R.string.ajax_processing).apply { setCancelable(false) }
        progressDialog.show()

        req = setRequest("wali.list_santri")
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            progressDialog.dismiss()
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    var rows = (dataRes.obj()["result"] as JSONObject).getString("rows")
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    santri = gson.fromJson(rows, object : TypeToken<List<tListSantri>>() {}.type)

                    if (santri.isEmpty()) {
                        recycleView.visibility = View.GONE
                    } else {
                        recycleView.visibility = View.VISIBLE
                        recycleView.layoutManager = LinearLayoutManager(context)
                        recycleView.adapter = ListAdapter(santri)
                    }

                } else {
                    val builder = AlertDialog.Builder((activity as AppCompatActivity))
                    builder.setTitle("Error")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK") { dialog, which ->
                        if (!dataRes.obj().isNull("need_login")) {
                            F().actionLogout((activity as AppCompatActivity))
                        }
                    }
                    builder.create().show()
                }
            } else {
                snackbar(main_layout, R.string.ajax_request_failed)
            }
        }
    }

    class ListAdapter(val rows: ArrayList<tListSantri>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_santri, p0, false))
        }

        override fun getItemCount(): Int = rows.size

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {

                txt_name.text = r.full_name
                txt_nis.text = r.reg_no
                txt_nis.tag = r.partner_id

                btn_bill.setOnClickListener {
                    pubVar.put("nis", r.reg_no)
                    pubVar.put("name", r.full_name)

                    context?.startActivity(Intent(context, BillingActivity::class.java))
                }
                btn_transaction.setOnClickListener {
                    pubVar.put("nis", r.reg_no)
                    pubVar.put("name", r.full_name)

                    context?.startActivity(Intent(context, TransactionActivity::class.java))
                }
            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
