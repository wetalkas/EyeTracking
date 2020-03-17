package com.wet.eyetracking.ui.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.FragmentStatePagerAdapter
import com.wet.eyetracking.R
import kotlinx.android.synthetic.main.fragment_experiment.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ExperimentFragment : Fragment() {



    internal var id: Long = 0

    companion object {

        private val ID = "id"

        fun newInstance(id: Long): ExperimentFragment {
            val args = Bundle()
            args.putLong(ID, id)
            val fragment = ExperimentFragment()
            fragment.arguments = args
            return fragment
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experiment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments!!.getLong(ID)

        viewpager.adapter = SampleFragmentPagerAdapter(activity?.supportFragmentManager, activity)
        tabBar.setupWithViewPager(viewpager)
    }


    inner class SampleFragmentPagerAdapter(fm: FragmentManager?, private val context: Context?) : FragmentStatePagerAdapter(fm) {
        internal val PAGE_COUNT = 2
        private val tabTitles = arrayOf("График", "Подробно")

        override fun getCount(): Int {
            return PAGE_COUNT
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> ChartFragment.newInstance(id)
                1 -> DetailsFragment.newInstance(id)
                else -> {
                    ChartFragment.newInstance(id)
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            // Generate title based on item position
            return tabTitles[position]
        }
    }


}
