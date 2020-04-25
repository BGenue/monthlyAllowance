package GenueProject.monthlyallowance;

import android.util.SparseBooleanArray;
import android.view.View;

public interface OnCategoryItemClickListener
{
	void onItemClick(CategoryAdapter.ViewHolder holder, View view, int position, SparseBooleanArray selectedItems);
}
