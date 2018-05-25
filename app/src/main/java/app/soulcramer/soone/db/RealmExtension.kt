@file:JvmName("RealmUtils")

// pretty name for utils class if called from Java
package app.soulcramer.soone.db

import com.zhuinden.monarchy.Monarchy

fun Monarchy.userDao(): UserDao = UserDao(this)
