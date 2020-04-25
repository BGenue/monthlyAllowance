package GenueProject.monthlyallowance;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements OnCategoryItemClickListener
{
	Context context;
	String TAG = "category";
	ArrayList<CategoryItem> list = null;
	OnCategoryItemClickListener listener = null;
	private SparseBooleanArray selectedItems = new SparseBooleanArray(0);

	public CategoryAdapter(ArrayList<CategoryItem> list, Context context)
	{
		this.list = list;
		this.context = context;
	}

	//뷰홀더 객체 생성 리턴
	@Override
	public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.category_item, parent, false);
		CategoryAdapter.ViewHolder viewHolder = new CategoryAdapter.ViewHolder(itemView, this);

		return new ViewHolder(itemView, this);
	}

	//아이템 표시
	@Override
	public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position)
	{
		Log.i(TAG, "onBindViewHolder");
		CategoryItem item = list.get(position);
		holder.name.setText(item.getName());
		holder.setItem(item);
		selectedItems.put(position, false);
	}

	//아이템 갯수
	@Override
	public int getItemCount()
	{
		return list.size();
	}

	public void setOnItemClickListener(OnCategoryItemClickListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void onItemClick(ViewHolder holder, View view, int position, SparseBooleanArray selectedItems)
	{
		if(listener != null)
		{
			listener.onItemClick(holder, view, position, selectedItems);
		}
	}

	public void addItem(CategoryItem item)
	{
		list.add(item);
	}

	public void addItems(ArrayList<CategoryItem> items)
	{
		list = items;
	}

	public CategoryItem getItem(int position)
	{
		return list.get(position);
	}

	//아이템 뷰 저장하는 뷰홀더
	public class ViewHolder extends RecyclerView.ViewHolder
	{
		TextView name;

		public ViewHolder(View view, final OnCategoryItemClickListener listener)
		{
			super(view);
			name = view.findViewById(R.id.category_item_text);
			name.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					int position = getAdapterPosition();
					if(position != RecyclerView.NO_POSITION)
					{
						if(listener != null)
						{
							listener.onItemClick(ViewHolder.this, v, position, selectedItems);
						}
					}
				}
			});
		}

		public void setItem(CategoryItem item)
		{
			name.setText(item.getName());
		}
	}
}
