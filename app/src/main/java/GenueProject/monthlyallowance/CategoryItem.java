package GenueProject.monthlyallowance;

public class CategoryItem
{
	private String name;
	private boolean checked;

	public CategoryItem() {}

	public CategoryItem(String name)
	{
		this.name = name;
		checked = false;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setChecked(boolean checked) { this.checked = checked; }

	public boolean getChecked() { return checked; }
}
