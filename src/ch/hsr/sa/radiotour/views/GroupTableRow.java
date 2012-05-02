package ch.hsr.sa.radiotour.views;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Group;

public class GroupTableRow extends TableRow {
	private TextView description;
	private TextView time;
	private final Map<Integer, LinearLayout> map = new HashMap<Integer, LinearLayout>();
	private LinearLayout odd, even;

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
		LayoutInflater.from(context).inflate(
				R.layout.group_table_row_ingredient, this);
		odd = (LinearLayout) findViewById(R.id.llayout_driver_odd);
		even = (LinearLayout) findViewById(R.id.llayout_driver_even);
		description = (TextView) findViewById(R.id.txt_description);
		time = (TextView) findViewById(R.id.txt_group_time);
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
			// txtViewToAdd.setText(riderNr + "");
			txtViewToAdd.setText(((RadioTour) context.getApplicationContext())
					.getRidersAsMap().get(riderNr).toString());
			txtViewToAdd.setTextColor(Color.WHITE);
			txtViewToAdd.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			txtViewToAdd.setMinimumWidth(40);
			txtViewToAdd.setGravity(Gravity.LEFT);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			layoutParams.setMargins(3, 3, 3, 3);
			txtViewToAdd.setLayoutParams(layoutParams);

			LinearLayout temp = even.getChildCount() < odd.getChildCount() ? even
					: odd;
			temp.addView(txtViewToAdd);
			map.put(riderNr, temp);

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
			txtViewToAdd.setSingleLine(true);
			txtViewToAdd.setOnClickListener((RadioTourActivity) context);
		}
	}

	public boolean hasRiderNr(Integer riderNr) {
		return map.containsKey(riderNr);
	}

	private LinearLayout getParentLayout(Integer riderNr) {
		return map.get(riderNr);
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
		LinearLayout temp = getParentLayout(driverNr);
		if (temp != null) {
			for (int i = 0; i < temp.getChildCount(); i++) {
				if (((TextView) temp.getChildAt(i))
						.getText()
						.toString()
						.equals(((RadioTour) context.getApplicationContext())
								.getRidersAsMap().get(driverNr).toString())) {
					temp.removeViewAt(i);
					map.remove(driverNr);
				}
			}
		}

	}
}
