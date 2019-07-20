package com.example.DalwaApp.helper

import android.content.Context
import android.widget.Toast

/**
 * Created by antho.firuze@gmail.com on 2018-07-05.
 */

inline fun Context.lToast(msg: CharSequence): Toast = Toast.makeText(this, msg, Toast.LENGTH_LONG).apply { show() }
inline fun Context.sToast(msg: CharSequence): Toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT).apply { show() }
