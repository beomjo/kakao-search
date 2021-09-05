package io.github.beomjo.search.navigator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.annotation.IdRes
import androidx.core.content.res.use
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import io.github.beomjo.search.R
import java.util.ArrayDeque
import java.util.Deque

@Navigator.Name("custom")
class CustomNavigator(
    @IdRes private val fragmentContainerId: Int,
    private val fragmentManager: FragmentManager
) : Navigator<CustomNavigator.Destination>() {
    private val tabBackStack: Deque<String> = ArrayDeque()
    private val backStack: Deque<String> = ArrayDeque()

    override fun createDestination(): Destination = Destination(this)

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ): NavDestination? {
        val className = destination.className ?: return null
        val tag = className.split('.').last()
        return if (Tab.isContainsTag(tag)) {
            navigateToTab(tag, className, destination)
        } else {
            navigateToOther(tag, className, destination, args)
        }
    }

    private fun navigateToTab(
        tag: String,
        className: String,
        destination: Destination
    ): Destination? {
        if (tabBackStack.peekLast() == tag) {
            return null
        }

        if (tabBackStack.peekLast() != tag) {
            tabBackStack.addLast(tag)
        }

        val current = fragmentManager.findFragmentByTag(tag)
        fragmentManager.commit {
            if (current == null) {
                val fragment = getFragmentFromClassName(className)
                add(fragmentContainerId, fragment, tag)
            } else {
                show(current)
            }

            hideOthers(tag)
        }

        return destination
    }

    private fun navigateToOther(
        tag: String,
        className: String,
        destination: Destination,
        args: Bundle?,
    ): Destination {
        fragmentManager.commit {
            hideAllTab()
            add(
                fragmentContainerId,
                getFragmentFromClassName(className).apply {
                    arguments = args
                },
                tag
            )

            if (backStack.peekLast() != tag) {
                backStack.addLast(tag)
            }
        }
        return destination
    }

    override fun popBackStack(): Boolean {
        val tag = backStack.pollLast()
        val hasOnlyTabBackStack = tag == null
        return if (hasOnlyTabBackStack) {
            popTabBackStack()
        } else {
            popOtherBackStack(tag)
        }
    }

    private fun popTabBackStack(): Boolean {
        val tabTag = tabBackStack.pollLast() ?: return true
        val newCurrentTag = tabBackStack.peekLast() ?: return true
        fragmentManager.commit {
            getFragmentByTag(newCurrentTag)?.let {
                show(it)
                hideOthers(newCurrentTag)
            }
        }
        return true
    }

    private fun popOtherBackStack(tag: String?): Boolean {
        fragmentManager.commit {
            fragmentManager.findFragmentByTag(tag)?.let {
                remove(it)
            }
        }
        val newCurrentTag = backStack.peekLast()
        if (newCurrentTag == null) {
            fragmentManager.commit {
                getFragmentByTag(tabBackStack.peekLast())?.let {
                    hideAllTab()
                    show(it)
                }
            }
        }
        return true
    }

    private fun getFragmentByTag(tag: String?): Fragment? {
        return fragmentManager.findFragmentByTag(tag)
    }

    private fun getFragmentFromClassName(className: String): Fragment {
        return fragmentManager.fragmentFactory.instantiate(
            ClassLoader.getSystemClassLoader(),
            className
        )
    }

    private fun FragmentTransaction.hideOthers(tag: String) {
        Tab.otherTab(exceptTag = tag)
            .mapNotNull { fragmentManager.findFragmentByTag(it.tag) }
            .forEach { hide(it) }
    }

    private fun FragmentTransaction.hideAllTab() {
        Tab.values()
            .mapNotNull { fragmentManager.findFragmentByTag(it.tag) }
            .forEach { hide(it) }
    }

    override fun onSaveState(): Bundle {
        return bundleOf(
            KEY_TAB_BACK_STACK to tabBackStack.toTypedArray(),
            KEY_BACK_STACK to backStack.toTypedArray()
        )
    }

    override fun onRestoreState(savedState: Bundle) {
        savedState.getStringArray(KEY_TAB_BACK_STACK)?.let {
            tabBackStack.clear()
            tabBackStack.addAll(it)
        }
        savedState.getStringArray(KEY_BACK_STACK)?.let {
            backStack.clear()
            backStack.addAll(it)
        }
    }

    @NavDestination.ClassType(Fragment::class)
    class Destination(navigator: CustomNavigator) : NavDestination(navigator) {
        internal var className: String? = null
            private set

        @SuppressLint("MissingSuperCall")
        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)
            className = context.resources.obtainAttributes(attrs, R.styleable.BottomNavigator)
                .use { it.getString(R.styleable.BottomNavigator_android_name) }
        }
    }

    companion object {
        private const val KEY_TAB_BACK_STACK = "CustomNavigator.TabBackStack"
        private const val KEY_BACK_STACK = "CustomNavigator.BackStack"
    }
}
