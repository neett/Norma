package roskar.kristjan.norma

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.databinding.ListItemBinding

class Adapter (private val itemList: ArrayList<ItemList>) : RecyclerView.Adapter<Adapter.MyViewHolder>() {
    var clickedPosition = 82
    inner class MyViewHolder(binding: ListItemBinding, listener: onItemClickListener): RecyclerView.ViewHolder(binding.root) {
        val listItemDate = binding.listItemDate
        val listItemNormaHours = binding.listItemNormaHours
        val listItemWorkingHours = binding.listItemWorkingHours
        val listItemWorkplace = binding.listItemWorkplace
        val listItemAdd = binding.listItemAdd
        val listItemRemove = binding.listItemRemove

        init {
            binding.root.setOnClickListener{

                listener.onItemClick(adapterPosition)
                listItemRemove.visibility = View.VISIBLE
                listItemAdd.visibility = View.VISIBLE
                clickedPosition = adapterPosition

                Log.d("adapterposition","adapterposition $adapterPosition")

            }
            listItemAdd.setOnClickListener {
                listener.onAddButtonClicked(adapterPosition)
            }
        }
    }

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
        fun onAddButtonClicked(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,mListener)

    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (clickedPosition == position) {
            holder.listItemAdd.visibility = View.VISIBLE
            holder.listItemRemove.visibility = View.VISIBLE
        }
        val currentItem = itemList[position]
        Log.d("itemcur",currentItem.toString())

        val dateFormatted = Date.formatDate(currentItem.date.toString(),"dMMyy","dd. EE")
        holder.listItemDate.text = dateFormatted
        //holder.listItemHours.setText(currentItem.hours.toString())
        holder.listItemNormaHours.text = currentItem.normaHours.toString()
        holder.listItemWorkingHours.text = currentItem.workingHours.toString()
        holder.listItemWorkplace.text = currentItem.workplace
        //holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}