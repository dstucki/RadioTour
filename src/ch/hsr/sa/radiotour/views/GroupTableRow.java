package ch.hsr.sa.radiotour.views;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.TreeSet;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
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
		time.setText(getHandicapAsString());
		time.setClickable(true);
		// time.setLayoutParams(getTextViewMargins());
		time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((RadioTourActivity) context).show(GroupTableRow.this);
			}
		});

		description.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				ClipData data = ClipData.newPlainText(description.getText(),
						description.getText());
				return v.startDrag(data, new DragShadowBuilder(
						GroupTableRow.this), group.getDriverNumbers(), 0);
			}
		});

		description.setTextSize(30);

		this.addView(time);
		this.addView(description);
	}

	public LinearLayout.LayoutParams getTextViewMargins() {
		LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		myParams.setMargins(5, 5, 5, 5);
		return myParams;
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
		time.setText(getHandicapAsString());
	}

	public String getHandicapAsString() {
		final NumberFormat formatter = new DecimalFormat("00");
		final Date date = group.getHandicapTime();
		if (date == null) {
			return "00:00:00";
		}
		final StringBuilder tempString = new StringBuilder();
		tempString.append(formatter.format(date.getHours()));
		tempString.append(":");
		tempString.append(formatter.format(date.getMinutes()));
		tempString.append(":");
		tempString.append(formatter.format(date.getSeconds()));
		return tempString.toString();

	}

	public void addRider(final Integer riderNr) {
		group.getDriverNumbers().add(riderNr);

		if (!group.isField()) {
			final TextView txtViewToAdd = new TextView(context);
			txtViewToAdd.setId(riderNr);
			txtViewToAdd.setText(riderNr + "");
			// txtViewToAdd.setText(((RadioTour)
			// context.getApplicationContext())
			// .getRidersAsMap().get(riderNr).toString());
			txtViewToAdd.setTextColor(Color.WHITE);
			txtViewToAdd.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			txtViewToAdd.setMinimumWidth(40);
			txtViewToAdd.setGravity(Gravity.RIGHT);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			layoutParams.setMargins(3, 3, 3, 3);
			txtViewToAdd.setLayoutParams(layoutParams);

			addView(txtViewToAdd);
			invalidate();
			txtViewToAdd.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					final TextView textView = (TextView) v;
					ClipData data = ClipData.newPlainText(textView.getText(),
							textView.getText());
					TreeSet<Integer> localState = new TreeSet<Integer>();
					localState.add(riderNr);
					v.startDrag(data, new DragShadowBuilder(txtViewToAdd),
							localState, 0);
					return true;
				}
			});

			txtViewToAdd.setOnClickListener((RadioTourActivity) context);
		}
	}

	@Override
	public void removeView(View view) {
		super.removeView(view);
		group.getDriverNumbers().remove(
				Integer.valueOf(((TextView) view).getText().toString()));
	}

	public void setHandicapTime(int hour, int minute, int seconds) {
		group.setHandicapTime(new Date(0, 0, 0, hour, minute, seconds));
		time.setText(getHandicapAsString());
	}

	public void removeRiderNr(Integer driverNr) {
		group.removeDriverNumber(driverNr);
		for (int i = 2; i < getChildCount(); i++) {
			if (((TextView) getChildAt(i)).getText().toString()
					.equals(driverNr + "")) {
				removeViewAt(i);
			}
		}

	}
}
