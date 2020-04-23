package GenueProject.monthlyallowance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>
{
	private ArrayList<CategoryItem> list = null;

	public CategoryAdapter(ArrayList<CategoryItem> list)
	{
		this.list = list;
	}

	//뷰홀더 객체 생성 리턴
	@Override
	public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
	{
		Context context = parent.getContext();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.category_item, parent, false);
		CategoryAdapter.ViewHolder viewHolder = new CategoryAdapter.ViewHolder(view);

		return viewHolder;
	}

	//아이템 표시
	@Override
	public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position)
	{
		CategoryItem item = list.get(position);
		holder.icon.setImageResource(R.mipmap.ic_launcher);
		holder.name.setText(item.getName());
	}

	//아이템 갯수
	@Override
	public int getItemCount()
	{
		return list.size();
	}

	//아이템 뷰 저장하는 뷰홀더
	public class ViewHolder extends RecyclerView.ViewHolder
	{
		ImageView icon;
		TextView name;

		ViewHolder(View view)
		{
			super(view);
			icon = view.findViewById(R.id.category_item_image);
			name = view.findViewById(R.id.category_item_text);
		}
	}
}
