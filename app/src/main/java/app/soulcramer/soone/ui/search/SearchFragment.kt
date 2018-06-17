package app.soulcramer.soone.ui.search

import `fun`.soone.R
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.soulcramer.soone.api.SooneService
import app.soulcramer.soone.common.observeK
import app.soulcramer.soone.di.Injectable
import app.soulcramer.soone.ui.user.UserViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchFragment : Fragment(), Injectable, SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var service: SooneService

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var searchParam: SooneService.SearchBody

    private val toolbar: ActionBar by lazy {
        (activity as AppCompatActivity).supportActionBar!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("sooneSharedPref", Context.MODE_PRIVATE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SearchViewModel::class.java)
        userViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(UserViewModel::class.java)

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        launchActiveChat(sharedPreferences)

        toolbar.title = "Recherche Instantanée"

        userViewModel.user.observeK(this) {
            it.data?.run {
                searchParam = SooneService.SearchBody(id ?: "", listOf(18, 25))
            }

        }

        button.setOnClickListener {
            launch {
                service.instantSearch(searchParam).enqueue(object : Callback<String?> {
                    override fun onFailure(call: Call<String?>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<String?>?, response: Response<String?>?) {
                    }
                })
            }
        }
    }


    override fun onDestroyView() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroyView()
    }

    override fun onSharedPreferenceChanged(sharedPreference: SharedPreferences, key: String) {
        if (key == "activeChatId" || key == "activeDecision") {
            launchActiveChat(sharedPreference)
        }
    }

    private fun launchActiveChat(sharedPreferences: SharedPreferences) {
        val activeChatId = sharedPreferences.getString("activeChatId", "")
        val activeDecision = sharedPreferences.getBoolean("activeDecision", false)
        val activeDecisionId = sharedPreferences.getString("activeDecisionId", "")
        if (activeDecision) {
            val action = SearchFragmentDirections.action_search_to_match2(activeChatId, activeDecisionId)
            action.setChatId(activeChatId)
            action.setDecisionId(activeDecisionId)
            findNavController().navigate(action)
        }
        if (activeChatId.isNotEmpty()) {
            val action = SearchFragmentDirections.action_search_to_chat2(activeChatId)
            action.setActiveChatId(activeChatId)
            findNavController().navigate(action)
        }
    }


}
