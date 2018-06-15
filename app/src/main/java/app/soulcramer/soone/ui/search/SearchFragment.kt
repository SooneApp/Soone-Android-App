package app.soulcramer.soone.ui.search

import `fun`.soone.R
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
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

class SearchFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var service: SooneService

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var searchParam: SooneService.SearchBody

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SearchViewModel::class.java)
        userViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(UserViewModel::class.java)


        userViewModel.user.observeK(this) {
            it.data?.run {
                searchParam = SooneService.SearchBody(id ?: "", listOf(18, 25))
                if (activeChatId.isNotEmpty()) {
                    val action = SearchFragmentDirections.actionSearchMatch()
                    findNavController().navigate(action)
                }
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
}
