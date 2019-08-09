package com.example.dalwaapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_confirm.*
import android.opengl.ETC1.getWidth
import android.widget.Toast
import org.jetbrains.anko.sdk25.coroutines.onGroupClick


class ConfirmActivity : AppCompatActivity() {

    val TAG = "ConfirmActivity"
    val header: MutableList<String> = ArrayList()
    val body: MutableList<MutableList<String>> = ArrayList()
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        btn_confirm.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.putExtra("EXIT", true)
//            startActivity(i)
            finish()
        }

        loadData()
        loadExpandableList()

        elv.setOnGroupClickListener { parent, v, groupPosition, id ->
            setListViewHeight(parent, groupPosition)
            return@setOnGroupClickListener false
        }

        myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

        lbl_copy.setOnClickListener {
            myClip = ClipData.newPlainText("text", lbl_account_no.text.toString())
            myClipboard?.primaryClip = myClip

            Toast.makeText(this, "No. Virtual Account telah di salin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setListViewHeight(parent: ExpandableListView?, group: Int) {
        val listAdapter = parent!!.expandableListAdapter as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )
        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, parent)
            groupItem!!.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)

            totalHeight += groupItem.measuredHeight

            if (parent.isGroupExpanded(i) && i != group || !parent.isGroupExpanded(i) && i == group) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem = listAdapter.getChildView(
                        i, j, false, null,
                        parent
                    )
                    listItem!!.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)

                    totalHeight += listItem.measuredHeight

                }
            }
        }

        val params = parent.layoutParams
        var height = totalHeight + parent.dividerHeight * (listAdapter.groupCount - 1)
        if (height < 10)
            height = 200
        params.height = height
        parent.layoutParams = params
        parent.requestLayout()
    }

    private fun loadData() {
        lbl_total.text = pubVar["grand_total"].toString()
        lbl_account_no.text = pubVar["account_no"].toString()
    }

    private fun loadExpandableList() {
        header.add("Petunjuk Transfer ATM")
        header.add("Petunjuk Transfer iBanking / KlikBCA")
        header.add("Petunjuk Transfer mBanking / m-BCA")

        body.add(
            mutableListOf(
                "1. Pilih Transaksi Lainnya > Transfer > Ke Rek BCA Virtual Account\n" +
                        "\n" +
                        "2. Masukkan nomor virtual account ${pubVar["account_no"].toString()} dan pilih Benar\n" +
                        "\n" +
                        "3. Periksa informasi yang tertera di layar. Pastikan Nama Account adalah YAY DARULLUGHAH WADDA'WAH, dan Total Tagihan sudah benar. Jika benar, pilih Ya"
            )
        )
        body.add(
            mutableListOf(
                "1. Pilih Transfer Dana > Transfer ke BCA Virtual Account\n" +
                        "\n" +
                        "2. Centang No. Virtual Account dan masukkan ${pubVar["account_no"].toString()}. Klik Lanjutkan\n" +
                        "\n" +
                        "3. Periksa informasi yang tertera di layar. Pastikan Nama Account adalah YAY DARULLUGHAH WADDA'WAH, dan Total Tagihan sudah benar. Jika benar, klik Lanjutkan\n" +
                        "\n" +
                        "4. Masukkan respon KeyBCA Anda dan klik Kirim"
            )
        )
        body.add(
            mutableListOf(
                "1. Pilih m-Transfer > BCA Virtual Account\n" +
                        "\n" +
                        "2. Masukkan nomor virtual account ${pubVar["account_no"].toString()} dan pilih Send\n" +
                        "\n" +
                        "3. Periksa informasi yang tertera di layar. Pastikan Nama Account adalah YAY DARULLUGHAH WADDA'WAH, dan Total Tagihan sudah benar. Jika benar, pilih OK\n" +
                        "\n" +
                        "4. Masukkan PIN m-BCA Anda dan pilih OK"
            )
        )

        elv.setAdapter(ExpandableListAdapter(this, header, body))
        elv.layoutParams.height = 120 * header.size
//        elv.expandGroup(0, true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class ExpandableListAdapter(
        var context: Context?, var header: MutableList<String>, var body: MutableList<MutableList<String>>
    ) : BaseExpandableListAdapter() {

        override fun getGroup(groupPosition: Int): Any = header[groupPosition]

        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

        override fun hasStableIds(): Boolean = false

        override fun getGroupView(
            groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?
        ): View? {

            var convertView = convertView
            if (convertView == null) {
                val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.list_faq_group, null)
            }
            val title = convertView!!.findViewById<TextView>(R.id.tv_title)
            title.text = getGroup(groupPosition).toString()
            return convertView
        }

        override fun getChildrenCount(groupPosition: Int): Int = body[groupPosition].count()

        override fun getChild(groupPosition: Int, childPosition: Int): Any = body[groupPosition][childPosition]

        override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

        override fun getChildView(
            groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?
        ): View? {
            var convertView = convertView
            if (convertView == null) {
                val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.list_faq_child, null)
            }
            val title = convertView!!.findViewById<TextView>(R.id.tv_title)
            title.text = getChild(groupPosition, childPosition).toString()
            title.textSize = 12F
            return convertView
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

        override fun getGroupCount(): Int = header.size

    }
}
