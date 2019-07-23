package com.example.DalwaApp.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.example.DalwaApp.*

import com.example.DalwaApp.model.tListSantri
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_santri.*
import kotlinx.android.synthetic.main.list_santri.view.*
import org.jetbrains.anko.design.snackbar
import org.json.JSONObject

class SantriFragment : Fragment() {

    var santri: ArrayList<tListSantri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_santri, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return when (item!!.itemId) {
//            R.id.opt_add -> {
//                Toast.makeText(context, "click on add", Toast.LENGTH_LONG).show()
//                val intent = Intent(context, ProfileEditActivity::class.java)
//                startActivity(intent)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun loadData() {
        if (! session!!.isLogin)
            return

//        req = setRequest("wali.list_santri", mapOf("partner_id" to partner_id))
//        URL_API.httpPost().body(req).responseJson { _, resp, res ->
//            if (resp.data.size > 0) {
//                val (dataRes, _) = res
//                if (dataRes!!.obj().getBoolean("status")) {
//                    var rows = (dataRes.obj()["result"] as JSONObject).getString("rows")
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    santri = gson.fromJson(rows, object : TypeToken<List<tListSantri>>() {}.type)
//
//                    if (santri.isEmpty()) {
//                        recycleView.visibility = View.GONE
//                    } else {
//                        recycleView.visibility = View.VISIBLE
//                        recycleView.layoutManager = LinearLayoutManager(context)
//                        recycleView.adapter = ListAdapter(santri)
//                    }
//
//                } else {
//                    snackbar(santri_layout, "Error Load Data: "+dataRes.obj().getString("message"))
//                }
//            } else {
//                snackbar(santri_layout,"Error: Ajax request failed")
//            }
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_santri, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        setHasOptionsMenu(true)

        loadData()
//        toolbar.setOnMenuItemClickListener {
//            Toast.makeText(context, it.title, Toast.LENGTH_LONG).show()
//
//            when (it.itemId) {
//                R.id.opt_add -> {
//                    val intent = Intent(context, LoginActivity::class.java)
//                    context?.startActivity(intent)
//                }
//                R.id.opt_logout -> {
//                    val builder =  AlertDialog.Builder((activity as AppCompatActivity))
//                    builder.setTitle("Info")
//                    builder.setMessage("Anda ingin keluar ?")
//                    builder.setPositiveButton("YES"){ dialog, which ->
//                        // Do something when user press the positive button
//                        Toast.makeText(context,"Ok, we logout.", Toast.LENGTH_SHORT).show()
//
////                        isLogin = false
//                        session!!.isLogin = false
//                        (activity as AppCompatActivity).invalidateOptionsMenu()
//
//                        (activity as AppCompatActivity).navigation.menu.findItem(R.id.nav_profile_wali).isVisible = false
//                        (activity as AppCompatActivity).navigation.menu.findItem(R.id.nav_profile_santri).isVisible = false
//                    }
//                    builder.setNeutralButton("Cancel"){ dialog, which -> }
//                    val dialog: AlertDialog = builder.create()
//                    dialog.show()
//                }
//                R.id.opt_signup -> {
//                    val intent = Intent(context, RegisterActivity::class.java)
//                    context?.startActivity(intent)
//                }
//                R.id.opt_chgpwd -> {
//                    val intent = Intent(context, ChangePwdActivity::class.java)
//                    context?.startActivity(intent)
//                }
//                R.id.opt_profile -> {
//                    val intent = Intent(context, ProfileActivity::class.java)
//                    context?.startActivity(intent)
//                }
//            }
//
//            return@setOnMenuItemClickListener true
//        }
    }

    class ListAdapter(val rows: ArrayList<tListSantri>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_santri, p0, false))
        }

        override fun getItemCount(): Int {
            return rows.size
        }

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {

                txt_name.text = r.full_name
                txt_nis.text = r.reg_no

                img_invoice.setOnClickListener { openInvoice(it, r.reg_no) }
                lbl_invoice.setOnClickListener { openInvoice(it, r.reg_no) }
                img_transaction.setOnClickListener { openTransaction(it, r.partner_id) }
                lbl_transaction.setOnClickListener { openTransaction(it, r.partner_id) }
            }
        }

        private fun openInvoice(view: View?, nis: String) {
            val intent = Intent(view?.context, ProfileActivity::class.java)
            view?.context?.startActivity(intent)
        }

        private fun openTransaction(view: View?, partnerId: Int) {
            val intent = Intent(view?.context, ProfileActivity::class.java)
            view?.context?.startActivity(intent)
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v) {}
    }
}
