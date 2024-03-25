package com.yw1573.tred.fragment

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yw1573.tred.SplashActivity
import com.yw1573.tred.adapter.OuterAdapter
import com.yw1573.tred.adapter.VerticalSpaceItemDecoration
import com.yw1573.tred.databinding.FragmentTableBinding
import com.yw1573.tred.data.DisplayData


class TableFragment : Fragment() {

    private var _binding: FragmentTableBinding? = null
    private val binding get() = _binding!!
    private lateinit var bloodSugarMap: Map<String, List<DisplayData>>
    private lateinit var adapter: OuterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTableBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OuterAdapter(requireActivity())
        bloodSugarMap = SplashActivity.dbHelper!!.bloodSugarNormalization()
        adapter.setBloodSugarMap(bloodSugarMap)

        val rv = binding.rv
        val spaceInPx = requireActivity().dpToPx(10)
        rv.addItemDecoration(VerticalSpaceItemDecoration(spaceInPx))
        rv.layoutManager = LinearLayoutManager(requireActivity())
        rv.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

fun Context.dpToPx(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
    ).toInt()
}
