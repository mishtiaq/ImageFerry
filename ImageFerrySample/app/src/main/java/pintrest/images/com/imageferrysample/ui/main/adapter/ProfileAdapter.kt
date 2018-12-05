package pintrest.images.com.imageferrysample.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pintrest.images.com.imageferry.ImageLoader
import pintrest.images.com.imageferrysample.R
import pintrest.images.com.imageferrysample.custom.UserClickListener
import pintrest.images.com.imageferrysample.data.UserWrapper

/**
 * Created by Ishtiaq on Dec , 04, 2018.
 */
class ProfileAdapter(val userList: ArrayList<UserWrapper>, val clickListener: UserClickListener) :
    RecyclerView.Adapter<ProfileAdapter.MyViewHolder>() {


    class MyViewHolder(viewGroup: ViewGroup) : RecyclerView.ViewHolder(viewGroup) {
        val mTextView = viewGroup.findViewById<TextView>(R.id.tvProfileName)
        val mImageView = viewGroup.findViewById<ImageView>(R.id.ivProfile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.profile_view, parent, false) as ViewGroup
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val wrapper = userList.get(position)
        holder.mTextView.text = wrapper.user.name
        ImageLoader.getInstance().load(holder.mImageView, wrapper.user.profileImage.medium)
        holder.itemView.setOnClickListener({ clickListener.onClick(wrapper.user, position) })
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
