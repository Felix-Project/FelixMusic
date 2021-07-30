package com.felix.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felix.arch.mvvm.BaseMvvmFragment
import com.felix.resp.SongBean
import com.felix.search.databinding.FragmentSearchBinding
import kotlin.math.abs

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : BaseMvvmFragment<SearchViewModel>() {

    lateinit var searchAdp: SearchAdp

    lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchAdp = SearchAdp()
        return FragmentSearchBinding.inflate(inflater, container, false).apply {
            binding = this
            rvMusicList.adapter = searchAdp
            rvMusicList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            etKeyword.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.search(etKeyword.text.toString())
                }
                true
            }
            rvMusicList.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (abs(scrollY - oldScrollY) > 30) {
                    hideKeyboard()
                }
                true
            }
        }.root
    }

    private fun hideKeyboard() {
        context?.getSystemService(Context.INPUT_METHOD_SERVICE)?.let {
            it as InputMethodManager
        }?.hideSoftInputFromWindow(binding.etKeyword.windowToken, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(viewModel.list) {
            searchAdp.datas = it
        }
    }

}