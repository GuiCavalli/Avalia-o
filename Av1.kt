<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp">

    <EditText
        android:id="@+id/edtTextDestino"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Destino" />

    <EditText
        android:id="@+id/editTextData"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Data" />

    <EditText
        android:id="@+id/editTextValor"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Valor Gasto"
        android:inputType="numberDecimal" />

    <Button
        android:id="@+id/buttonAdicionarViagem"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Adicionar Viagem" />

    <TextView
        android:id="@+id/textViewTotalGasto"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Total Gasto: R$ 0,00"
        android:textSize="18sp"
        android:layout_marginTop="20dp" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewViagens"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp" />

</LinearLayout>




  package br.unipar.avaliacao1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.unipar.avaliacao1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

     lateinit var binding: ActivityMainBinding
     val viagens = mutableListOf<Viagem>()
     lateinit var adapter: ViagemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ViagemAdapter(viagens)
        binding.recyclerViewViagens.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewViagens.adapter = adapter


        binding.buttonAdicionarViagem.setOnClickListener {
            val destino = binding.edtTextDestino.text.toString()
            val data = binding.editTextData.text.toString()
            val valor = binding.editTextValor.text.toString().toDoubleOrNull() ?: 0.0

            if (destino.isNotEmpty() && data.isNotEmpty() && valor > 0) {
                val novaViagem = Viagem(destino, data, valor)
                viagens.add(novaViagem)
                adapter.notifyDataSetChanged()
                atualizarTotalGasto()
                limparCampos()
            }
        }
    }

    private fun atualizarTotalGasto() {
        val totalGasto = viagens.sumOf { it.valor }
        binding.textViewTotalGasto.text = "Total Gasto: R$ %.2f".format(totalGasto)
    }

    private fun limparCampos() {
        binding.editTextDestino.text.clear()
        binding.editTextData.text.clear()
        binding.editTextValor.text.clear()
    }
}




package br.unipar.avaliacao1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Viagem(val destino: String, val data: String, val valor: Double)

class ViagemAdapter(private val viagens: List<Viagem>) : RecyclerView.Adapter<ViagemAdapter.ViagemViewHolder>() {

    class ViagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDestino: TextView = itemView.findViewById(R.id.textViewDestino)
        val textViewData: TextView = itemView.findViewById(R.id.textViewData)
        val textViewValor: TextView = itemView.findViewById(R.id.textViewValor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViagemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viagem, parent, false)
        return ViagemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViagemViewHolder, position: Int) {
        val viagem = viagens[position]
        holder.textViewDestino.text = viagem.destino
        holder.textViewData.text = viagem.data
        holder.textViewValor.text = "R$ %.2f".format(viagem.valor)
    }

    override fun getItemCount() = viagens.size
}
