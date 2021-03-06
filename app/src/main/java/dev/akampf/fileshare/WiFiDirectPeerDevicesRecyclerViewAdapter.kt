package dev.akampf.fileshare

import android.net.wifi.p2p.WifiP2pDevice
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import dev.akampf.fileshare.WiFiDirectDeviceFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_device.view.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
class WiFiDirectPeerDevicesRecyclerViewAdapter(
	private val mValues: List<WifiP2pDevice>,
	private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<WiFiDirectPeerDevicesRecyclerViewAdapter.DeviceViewHolder>() {

	private val mOnClickListener: View.OnClickListener

	init {
		setHasStableIds(true)
		mOnClickListener = View.OnClickListener { deviceView ->
			val wiFiDirectDevice = deviceView.tag as WifiP2pDevice
			// Notify the active callbacks interface (the activity, if the fragment is attached to
			// one) that an item has been selected.
			mListener?.onListFragmentInteraction(wiFiDirectDevice)
		}
	}


	// Create new views (invoked by the layout manager)
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
		val singleDeviceView = LayoutInflater.from(parent.context)
			.inflate(R.layout.fragment_device, parent, false)
		return DeviceViewHolder(singleDeviceView)
	}


	// Replace the contents of a view (representing a single device) through the ViewHolder (invoked by the layout manager)
	override fun onBindViewHolder(deviceViewHolder: DeviceViewHolder, position: Int) {
		// - get element from your data set at this position
		// - replace the contents of the view with that element
		val wiFiDirectDevice = mValues[position]
		deviceViewHolder.mIdView.text = wiFiDirectDevice.deviceAddress
		deviceViewHolder.mContentView.text = wiFiDirectDevice.deviceName

		with(deviceViewHolder.mView) {
			// Set tag for the view that can be clicked to an identifier for the content or the content itself so we can later retrieve
			// e.g. the data associated (if only identifier is used) and notify the containing activity of the click with this info, since
			// we only know which view was clicked
			tag = wiFiDirectDevice
			setOnClickListener(mOnClickListener)
		}
	}


	// Return the size of the data set (invoked by the layout manager)
	override fun getItemCount(): Int = mValues.size

	// Used to determine what items are the same, even when updating the whole list (instead of updating only affected items), decreases
	// resource usage and adds smooth transitions to added, removed and moved items in the list.
	// Just use mac address hex value converted to long cause it is 48 bit and thus smaller than a 64bit Long so can be used collision free.
	override fun getItemId(position: Int): Long {
		val wiFiDirectMacAddress: String = mValues[position].deviceAddress
		// TODO use assert here for expected form of mac address, what are the places to use assert?
		val macAddressHexStringWithoutColons = wiFiDirectMacAddress.replace(":", "")
		// radix 16 means we read the String as a hexadecimal number
		return macAddressHexStringWithoutColons.toLong(16)
	}

	inner class DeviceViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
		val mIdView: TextView = mView.item_number
		val mContentView: TextView = mView.content

		override fun toString(): String {
			return super.toString() + " '" + mContentView.text + "'"
		}
	}
}
