package com.example.DalwaApp.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.DalwaApp.*

import com.example.DalwaApp.model.tListSantri
import com.example.DalwaApp.model.tSantri
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.fragment_santri.*
import kotlinx.android.synthetic.main.list_santri.view.*
import org.json.JSONObject

class SantriFragment : Fragment() {

    var santri: MutableList<tListSantri> = mutableListOf()
    var santri2: ArrayList<tListSantri> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        recycleView.layoutManager = LinearLayoutManager(context)
//        var rows = Realm.getDefaultInstance().where<tSantri>().findAll()
//        if (! rows.isEmpty()) {
//            santri.addAll(Realm.getDefaultInstance().copyFromRealm(rows))
//            recycleView.adapter = ListAdapter(santri)
//        }

        req = setRequest("wali.list")
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.size > 0) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    var rows = (dataRes.obj()["result"] as JSONObject).getJSONArray("rows")
                    var l = rows.length()
//                    santri2 = ArrayList(rows.length()) { rows.get(it) }
//                    santri.addAll((dataRes.obj()["result"] as JSONObject).getJSONObject("rows"))
//                    santri2.addAll((dataRes.obj()["result"] as JSONObject).getJSONArray("rows"))
                    recycleView.layoutManager = LinearLayoutManager(context)
//                    recycleView.adapter = ListAdapter(santri)

//                    snackbar(main_layout, "Loading data berhasil (master)")
                } else {
//                    snackbar(main_layout, "Error Load Data: "+dataRes.obj().getString("message"))
                }
            } else {
//                snackbar(main_layout,"Error (Balance): Ajax request failed")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_santri, container, false)
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
