package ch.hsr.sa.radiotour.views;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.domain.Group;

public class GroupTableRow extends TableRow {
	private TextView description;
	private TextView time;

	public GroupTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.group = new Group();
		this.context = context;
		createUI();
	}

	private Group group;
	private final Context context;

	public GroupTableRow(Context context, Group group) {
		super(context);
		this.context = context;
		this.group = group;
		createUI();

	}

	private void createUI() {
		description = new TextView(context);
		time = new TextView(context);
		time.setText("0:00:00");
		time.setClickable(true);
		time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((RadioTourActivity) context).show(time);
			}
		});

		description.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				ClipData data = ClipData.newPlainText(description.getText(),
						description.getText());
				return v.startDrag(data, new DragShadowBuilder(
						GroupTableRow.this), GroupTableRow.this, 0);
			}
		});

		description.setTextSize(30);

		this.addView(time);
		this.addView(description);
	}

	public void changeDescription(String description) {
		this.description.setText(description);
	}

	public GroupTableRow(Context context) {
		super(context);
		group = new Group();
		this.context = context;
		createUI();
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
		if (group.isField()) {
			changeDescription(context.getString(R.string.field));
			this.description.setLongClickable(false);
		}
		for (int i : group.getDriverNumbers()) {
			addRider(i);
		}
	}

	public void addRider(Integer riderNr) {
		final TextView txtViewToAdd = new TextView(context);
		txtViewToAdd.setId(riderNr);
		txtViewToAdd.setText(riderNr + "");
		txtViewToAdd.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		txtViewToAdd.setMinimumWidth(40);
		txtViewToAdd.setGravity(Gravity.RIGHT);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		layoutParams.setMargins(3, 3, 3, 3);
		// txtViewToAdd.setBackgroundColor(Color.RED);
		txtViewToAdd.setLayoutParams(layoutParams);

		addView(txtViewToAdd);
		group.getDriverNumbers().add(riderNr);
		invalidate();
		txtViewToAdd.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				final TextView textView = (TextView) v;
				ClipData data = ClipData.newPlainText(textView.getText(),
						textView.getText());
				v.startDrag(data, new DragShadowBuilder(txtViewToAdd),
						txtViewToAdd, 0);
				return true;
			}
		});

	}

	@Override
	public void removeView(View view) {
		super.removeView(view);
		group.getDriverNumbers().remove(
				Integer.valueOf(((TextView) view).getText().toString()));
	}
}
