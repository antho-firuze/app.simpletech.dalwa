package com.example.dalwaapp.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import com.example.dalwaapp.R
import kotlinx.android.synthetic.main.fragment_faq.*

class FaqFragment : Fragment() {

    val header: MutableList<String> = ArrayList()
    val body: MutableList<MutableList<String>> = ArrayList()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Save the fragment's instance
        fragmentManager!!.putFragment(outState, "FaqFragment", this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            fragmentManager!!.getFragment(savedInstanceState, "FaqFragment")
//            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "FaqFragment");
        } else {
            header.add("Mengapa saya tidak menerima email dari Aplikasi?")
            header.add("Apakah password saya bisa di rubah?")
            header.add("Mengapa di halaman pesantren masih kosong?")
            header.add("Bagaimana saya mendapatkan NIS?")
            header.add("Bagaimana saya melakukan pembayaran?")
            header.add("Bagaimana saya melihat history pembayaran?")
            header.add("Dimana Contact Centre Dalwa Mobile?")

            body.add(
                mutableListOf(
                    "1. Selain folder Inbox, coba periksa juga folder kategori Social/Updates/Forums/Promotion \n" +
                            "\n" +
                            "2. Periksa juga folder spam atau junk email, setelah itu buka email kemudian Klik 'Laporkan bukan spam' atau 'Report not spam' \n"
                )
            )
            body.add(
                mutableListOf(
                    "Bisa. Pada halaman pesantren, pilih menu 'Ubah Password' di sebelah kanan atas. \n"
                )
            )
            body.add(
                mutableListOf(
                    "Karena anda belum menambahkan data santri pada menu plus di kanan atas. \n"
                )
            )
            body.add(
                mutableListOf(
                    "Anda bisa menanyakan NIS (No. Induk Siswa) pada Ustadz Kamar atau santri sendiri. \n"
                )
            )
            body.add(
                mutableListOf(
                    "Pada Halaman Pesantren. Klik tombol 'TAGIHAN' pada daftar siswa/santri masing-masing. \n"
                )
            )
            body.add(
                mutableListOf(
                    "Pada Halaman Pesantren. Klik tombol 'TRANSAKSI' pada daftar siswa/santri masing-masing. \n"
                )
            )
            body.add(
                mutableListOf(
                    "Sementara anda bisa menggunakan kontak dibawah ini: \n" +
                    "\n" +
                    "- Email: ahmad.firuze@gmail.com \n" +
                    "\n" +
                    "- WA: +62857-7797-4703 \n"
                )
            )

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        elv.setAdapter(ExpandableListAdapter(context, header, body))
        elv.layoutParams.height = 125 * header.size

        elv.setOnGroupClickListener { parent, v, groupPosition, id ->
            setListViewHeight(parent, groupPosition)
            return@setOnGroupClickListener false
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

    class ExpandableListAdapter(
        var context: Context?, var header: MutableList<String>, var body: MutableList<MutableList<String>>
    ) : BaseExpandableListAdapter() {

        override fun getGroup(groupPosition: Int): Any =  header[groupPosition]

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
            return convertView
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

        override fun getGroupCount(): Int = header.size

    }

}