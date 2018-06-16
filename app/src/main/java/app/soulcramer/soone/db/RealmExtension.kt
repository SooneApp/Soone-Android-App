@file:JvmName("RealmUtils")

// pretty name for utils class if called from Java
package app.soulcramer.soone.db

import com.zhuinden.monarchy.Monarchy

fun Monarchy.userDao(): UserDao = UserDao(this)
fun Monarchy.chatDao(): ChatDao = ChatDao(this)
fun Monarchy.messageDao(): MessageDao = MessageDao(this)
