package com.example.DalwaApp.product

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.DalwaApp.*
import com.example.DalwaApp.model.tPorfolio
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_product_info.*
import java.text.SimpleDateFormat

class ProductInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_info, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var row = realm?.where<tPorfolio>()?.equalTo("PortfolioID", pubVar?.get("PortfolioID") as Int)?.findFirst()

        tv_inv_goal.text = row?.InvestmentObjective
        tv_bank.text = row?.CustodianBank
        tv_date.text = SimpleDateFormat(FORM_DATE_FORMAT).format(row?.InceptionDate)
//        tv_date.text = SimpleDateFormat(FORM_DATETIME_FORMAT).format(stringToDateTime(row?.InceptionDate))
        tv_first_buy.text = row?.MinimumInitialSubscription
        tv_next_buy.text = row?.MinimumAdditionalSubscription
        tv_min_sell.text = row?.MinimumRedemption
        tv_cost_1.text = "Maksimal ${row?.FeeManagement}"
        tv_cost_2.text = "Maksimal ${row?.FeeCustodian}"
        tv_cost_3.text = "Maksimal ${row?.FeeRedemption}"
        tv_cost_4.text = "Maksimal ${row?.FeeSelling}"
    }
}