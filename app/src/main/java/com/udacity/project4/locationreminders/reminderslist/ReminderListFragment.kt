package com.udacity.project4.locationreminders.reminderslist

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.R
import com.udacity.project4.authentication.AuthenticationActivity
import com.udacity.project4.authentication.LoginViewModel
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentRemindersBinding
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import com.udacity.project4.utils.setTitle
import com.udacity.project4.utils.setup
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : BaseFragment() {
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentRemindersBinding
    override val _myViewModel: RemindersListViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_reminders, container, false
            )
        binding.viewModel = _myViewModel

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(false)
        setTitle(getString(R.string.app_name))

        binding.refreshLayout.setOnRefreshListener { _myViewModel.loadReminders() }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()

        binding.addReminderFAB.setOnClickListener {
            Toast.makeText(requireContext(), "LETS ADD REMINDER!!", Toast.LENGTH_SHORT).show()
            navigateToAddReminder()
        }
    }

    override fun onResume() {
        super.onResume()
        _myViewModel.loadReminders()
    }

    private fun navigateToAddReminder() {
        _myViewModel.navCommand.postValue(
            NavigationCommand.To(
                ReminderListFragmentDirections.toSaveReminder()
            )
        )
    }

    private fun navigateToBack() {
        val i = Intent(activity, AuthenticationActivity::class.java)
        startActivity(i)

    }


    private fun setupRecyclerView() {
        val adapter = RemindersListAdapter {
        }

        binding.reminderssRecyclerView.setup(adapter)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                viewModel.authenticationState.observe(
                    viewLifecycleOwner
                ) { authenticationState ->
                    when (authenticationState) {
                        LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                            AuthUI.getInstance().signOut(requireContext())
                            navigateToBack()
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)

    }
}
