package com.eccard.popularmovies.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection


abstract class BaseFragment<T: ViewDataBinding>: Fragment() {

    private var parentActivity: BaseActivity<*>? = null
    private var mRootView: View? = null
    private lateinit var mViewDataBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mRootView = mViewDataBinding.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.executePendingBindings()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            val activity = context as BaseActivity<*>?
            this.parentActivity = activity
            activity?.onFragmentAttached()
        }
    }

    override fun onDetach() {
        parentActivity = null
        super.onDetach()
    }

    interface CallBack {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }

    private fun performDI() = AndroidSupportInjection.inject(this)

    @LayoutRes
    abstract fun getLayoutId(): Int


    fun getBaseActivity(): BaseActivity<*>? {
        return parentActivity
    }

    fun getViewDataBinding(): T {
        return mViewDataBinding
    }

}