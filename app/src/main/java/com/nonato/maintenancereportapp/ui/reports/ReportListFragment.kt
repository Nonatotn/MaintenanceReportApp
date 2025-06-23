package com.nonato.maintenancereportapp.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nonato.maintenancereportapp.viewmodel.ReportsViewModel

class ReportListFragment : Fragment() {
    private lateinit var binding: FragmentReportListBinding
    private lateinit var viewModel: ReportsViewModel
    private lateinit var adapter: ReportListAdapter  // adapter personalizado

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReportListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(ReportsViewModel::class.java)
        adapter = ReportListAdapter { report ->
            // Ao clicar num relatório, navega para ReportDetailFragment passando o reportId
            val action = ReportListFragmentDirections.actionReportListToReportDetail(report.id)
            findNavController().navigate(action)
        }
        binding.recyclerReports.adapter = adapter
        binding.recyclerReports.layoutManager = LinearLayoutManager(context)

        // Observa LiveData de relatórios
        viewModel.allReports.observe(viewLifecycleOwner) { reports ->
            adapter.submitList(reports)
        }

        // Botão para adicionar novo relatório (pode abrir fragmento vazio)
        binding.fabAddReport.setOnClickListener {
            val action = ReportListFragmentDirections.actionReportListToReportDetail(0L)
            findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter.apply {
            onItemClick = { report ->
                // Cria a ação tipada gerada pelo Safe Args:
                val action = ReportListFragmentDirections
                    .actionReportListToReportDetail(report.id)

                // Executa a navegação
                findNavController().navigate(action)
            }
        }

    }
}


