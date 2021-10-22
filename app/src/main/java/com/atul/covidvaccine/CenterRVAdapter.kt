package com.atul.covidvaccine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CenterRVAdapter(private val centerList: List<CenterRVModel>): RecyclerView.Adapter<CenterRVAdapter.CenterRVViewHolder>() {

    class CenterRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val centerNameTV: TextView = itemView.findViewById(R.id.idTVCenterName);
        val centerAddressTV: TextView = itemView.findViewById(R.id.idTVCenterLocation);
        val centerTimingTV: TextView = itemView.findViewById(R.id.idTVCenterTiming);
        val vaccineNameTV: TextView = itemView.findViewById(R.id.idTVVaccineName);
        val vaccineNFeesTV: TextView = itemView.findViewById(R.id.idTVVaccineFees);
        val ageLimitTV: TextView = itemView.findViewById(R.id.idAgeLimit);
        val availabilityTV: TextView = itemView.findViewById(R.id.idAvailability);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterRVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout,parent,false)
        val viewHolder = CenterRVViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: CenterRVViewHolder, position: Int) {
        val center = centerList[position]

        holder.centerNameTV.text = center.centerName
        holder.centerAddressTV.text = center.centerAddress
        holder.centerTimingTV.text = ("From : ${center.centerFromTime}  To : ${center.centerToTime}")
        holder.vaccineNameTV.text = center.vaccineName
        holder.vaccineNFeesTV.text = center.fee_type
        holder.ageLimitTV.text = ("Age Limit : ${center.ageLimit.toString()}")
        holder.availabilityTV.text = ("Availability : ${center.availableCapacity.toString()}")

    }

    override fun getItemCount(): Int {
        return centerList.size;
    }
}