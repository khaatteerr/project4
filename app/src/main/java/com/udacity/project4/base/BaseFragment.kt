package com.udacity.project4.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


abstract class BaseFragment : Fragment() {

    abstract val _myViewModel: BaseViewModel

    override fun onStart() {
        super.onStart()

        _myViewModel.showToast.observe(this) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        }

        _myViewModel.snackBarInt.observe(this) {
            Snackbar.make(this.requireView(), getString(it), Snackbar.LENGTH_LONG).show()
        }

        _myViewModel.errorMessage.observe(this) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        }

        _myViewModel.snackBar.observe(this) {
            Snackbar.make(this.requireView(), it, Snackbar.LENGTH_LONG).show()
        }

        _myViewModel.navCommand.observe(this) { command ->
            when (command) {
                is NavigationCommand.BackTo -> findNavController().popBackStack(command.destId, false)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.To -> findNavController().navigate(command.directions)
            }
        }
    }
}