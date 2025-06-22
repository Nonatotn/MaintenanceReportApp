package ui.reports

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
    }
}

class ReportListAdapter(private val onClick: (Report) -> Unit) :
    ListAdapter<Report, ReportListAdapter.ReportViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTv: TextView = itemView.findViewById(R.id.tvReportTitle)
        private val clientTv: TextView = itemView.findViewById(R.id.tvReportClient)
        fun bind(report: Report) {
            titleTv.text = report.title
            clientTv.text = "Cliente: ${report.client}"
            itemView.setOnClickListener { onClick(report) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Report>() {
        override fun areItemsTheSame(old: Report, new: Report) = old.id == new.id
        override fun areContentsTheSame(old: Report, new: Report) = old == new
    }
}

