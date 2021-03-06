package be.mickverm.viewbinding

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


fun <V : View> View.bindView(@IdRes id: Int): ReadOnlyProperty<View, V> =
    required(id, viewFinder)

fun <V : View> View.bindViews(vararg ids: Int): ReadOnlyProperty<View, List<V>> =
    required(ids, viewFinder)

fun <V : View> View.bindOptionalView(@IdRes id: Int): ReadOnlyProperty<View, V?> =
    optional(id, viewFinder)

fun <V : View> View.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<View, List<V>> =
    optional(ids, viewFinder)


fun <V : View> Activity.bindView(@IdRes id: Int): ReadOnlyProperty<Activity, V> =
    required(id, viewFinder)

fun <V : View> Activity.bindViews(vararg ids: Int): ReadOnlyProperty<Activity, List<V>> =
    required(ids, viewFinder)

fun <V : View> Activity.bindOptionalView(@IdRes id: Int): ReadOnlyProperty<Activity, V?> =
    optional(id, viewFinder)

fun <V : View> Activity.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<Activity, List<V>> =
    optional(ids, viewFinder)


fun <V : View> Dialog.bindView(@IdRes id: Int): ReadOnlyProperty<Dialog, V> =
    required(id, viewFinder)

fun <V : View> Dialog.bindViews(vararg ids: Int): ReadOnlyProperty<Dialog, List<V>> =
    required(ids, viewFinder)

fun <V : View> Dialog.bindOptionalView(@IdRes id: Int): ReadOnlyProperty<Dialog, V?> =
    optional(id, viewFinder)

fun <V : View> Dialog.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<Dialog, List<V>> =
    optional(ids, viewFinder)


fun <V : View> DialogFragment.bindView(@IdRes id: Int): ReadOnlyProperty<DialogFragment, V> =
    required(id, viewFinder)

fun <V : View> DialogFragment.bindViews(vararg ids: Int): ReadOnlyProperty<DialogFragment, List<V>> =
    required(ids, viewFinder)

fun <V : View> DialogFragment.bindOptionalView(@IdRes id: Int): ReadOnlyProperty<DialogFragment, V?> =
    optional(id, viewFinder)

fun <V : View> DialogFragment.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<DialogFragment, List<V>> =
    optional(ids, viewFinder)


fun <V : View> Fragment.bindView(@IdRes id: Int): ReadOnlyProperty<Fragment, V> =
    required(this, id, viewFinder)

fun <V : View> Fragment.bindViews(vararg ids: Int): ReadOnlyProperty<Fragment, List<V>> =
    required(this, ids, viewFinder)

fun <V : View> Fragment.bindOptionalView(@IdRes id: Int): ReadOnlyProperty<Fragment, V?> =
    optional(this, id, viewFinder)

fun <V : View> Fragment.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<Fragment, List<V>> =
    optional(this, ids, viewFinder)


fun <V : View> RecyclerView.ViewHolder.bindView(@IdRes id: Int): ReadOnlyProperty<RecyclerView.ViewHolder, V> =
    required(id, viewFinder)

fun <V : View> RecyclerView.ViewHolder.bindViews(vararg ids: Int): ReadOnlyProperty<RecyclerView.ViewHolder, List<V>> =
    required(ids, viewFinder)

fun <V : View> RecyclerView.ViewHolder.bindOptionalView(@IdRes id: Int): ReadOnlyProperty<RecyclerView.ViewHolder, V?> =
    optional(id, viewFinder)

fun <V : View> RecyclerView.ViewHolder.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<RecyclerView.ViewHolder, List<V>> =
    optional(ids, viewFinder)


private typealias Finder<T> = T.(Int) -> View?

private val View.viewFinder: Finder<View>
    get() = { findViewById(it) }

private val Activity.viewFinder: Finder<Activity>
    get() = { findViewById(it) }

private val Dialog.viewFinder: Finder<Dialog>
    get() = { findViewById(it) }

private val DialogFragment.viewFinder: Finder<DialogFragment>
    get() = { dialog?.findViewById(it) ?: view?.findViewById(it) }

private val Fragment.viewFinder: Finder<Fragment>
    get() = { view!!.findViewById(it) }

private val RecyclerView.ViewHolder.viewFinder: Finder<RecyclerView.ViewHolder>
    get() = { itemView.findViewById(it) }


private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing =
    throw IllegalStateException("View ID $id for '${desc.name}' not found.")


@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: Finder<T>) =
    Lazy { t: T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(ids: IntArray, finder: Finder<T>) =
    Lazy { t: T, desc -> ids.map { t.finder(it) as V? ?: viewNotFound(it, desc) } }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(id: Int, finder: Finder<T>) =
    Lazy { t: T, _ -> t.finder(id) as V? }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(ids: IntArray, finder: Finder<T>) =
    Lazy { t: T, _ -> ids.map { t.finder(it) as V? }.filterNotNull() }


@Suppress("UNCHECKED_CAST")
private fun <V : View> required(fragment: Fragment, id: Int, finder: Finder<Fragment>) =
    LazyFragment(fragment) { t: Fragment, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

@Suppress("UNCHECKED_CAST")
private fun <V : View> required(fragment: Fragment, ids: IntArray, finder: Finder<Fragment>) =
    LazyFragment(fragment) { t: Fragment, desc -> ids.map { t.finder(it) as V? ?: viewNotFound(it, desc) } }

@Suppress("UNCHECKED_CAST")
private fun <V : View> optional(fragment: Fragment, id: Int, finder: Finder<Fragment>) =
    LazyFragment(fragment) { t: Fragment, _ -> t.finder(id) as V? }

@Suppress("UNCHECKED_CAST")
private fun <V : View> optional(fragment: Fragment, ids: IntArray, finder: Finder<Fragment>) =
    LazyFragment(fragment) { t: Fragment, _ -> ids.map { t.finder(it) as V? }.filterNotNull() }


private class Lazy<T, V>(
    private val initializer: (T, KProperty<*>) -> V
) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }

        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}

private class LazyFragment<V>(
    fragment: Fragment,
    private val initializer: (Fragment, KProperty<*>) -> V
) : ReadOnlyProperty<Fragment, V>, LifecycleObserver {
    private object EMPTY

    private var value: Any? = EMPTY

    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment, Observer {
            it.lifecycle.addObserver(this)
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }

        @Suppress("UNCHECKED_CAST")
        return value as V
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onViewDestroyed() {
        value = EMPTY
    }
}
