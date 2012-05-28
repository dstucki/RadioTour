package ch.hsr.sa.radiotour.views;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
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
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.fragments.DriverGroupFragment;
import ch.hsr.sa.radiotour.fragments.interfaces.TimePickerIF;
import ch.hsr.sa.radiotour.utils.StringUtils;

public class GroupTableRow extends TableRow implements TimePickerIF {
	private TextView description;
	private TextView time, lastTime;
	private final SparseArray<LinearLayout> map = new SparseArray<LinearLayout>();
	private final Map<Integer, TextView> mapTextView = new HashMap<Integer, TextView>();
	private LinearLayout odd, even;
	private Group group;
	private final Context context;
	private RadioTour app;
	private boolean isdirty = false;
	private DriverGroupFragment fragment;

	public GroupTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.group = new Group();
		this.context = context;
		createUI();
	}

	public GroupTableRow(Context context, Group group) {
		super(context);
		this.context = context;
		this.group = group;
		createUI();

	}

	private void createUI() {
		app = (RadioTour) context.getApplicationContext();

		LayoutInflater.from(context).inflate(
				R.layout.group_table_row_ingredient, this);
		odd = (LinearLayout) findViewById(R.id.llayout_driver_odd);
		even = (LinearLayout) findViewById(R.id.llayout_driver_even);
		description = (TextView) findViewById(R.id.txt_description);
		time = (TextView) findViewById(R.id.txt_group_time);
		lastTime = (TextView) findViewById(R.id.txt_group_last_time);
		setDeficits();
		time.setText(StringUtils.getTimeAsString(group.getHandicapTime()));
		time.setClickable(true);
		time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!group.isLeader())
					((RadioTourActivity) context).showTimeDialog(
							GroupTableRow.this, false);
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
		description.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (TextView t : mapTextView.values()) {
					t.performClick();
				}
			}
		});

		description.setTextSize(30);
		time.setTextSize(25);
		lastTime.setTextSize(18);

	}

	public void changeDescription(String description) {
		this.description.setText(description);
	}

	public void rearrangeTextViews() {
		if (group.isField() || !isdirty) {
			return;
		}
		int count = 1;
		for (Integer riderNr : group.getDriverNumbers()) {
			map.get(riderNr).removeView(mapTextView.get(riderNr));
			LinearLayout temp = count % 2 == 0 ? even : odd;
			temp.addView(mapTextView.get(riderNr));
			map.put(riderNr, temp);
			count++;
		}
		isdirty = false;
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
		setDeficits();
	}

	public void addRider(final Integer riderNr) {
		group.getDriverNumbers().add(riderNr);
		RiderStageConnection conn = app.getRiderStage(riderNr);
		conn.setVirtualDeficit(group.getHandicapTime());
		if (!group.isField()) {
			final TextView txtViewToAdd = createTxtViewToAdd(riderNr, conn);
			final LinearLayout temp = even.getChildCount() < odd
					.getChildCount() ? even : odd;
			temp.addView(txtViewToAdd);
			map.put(riderNr, temp);
			mapTextView.put(riderNr, txtViewToAdd);
			invalidate();

		}
		isdirty = true;
	}

	private TextView createTxtViewToAdd(final Integer riderNr,
			RiderStageConnection conn) {
		final TextView txtViewToAdd = new TextView(context);
		txtViewToAdd.setId(riderNr);
		txtViewToAdd.setText(conn.getRider().toString() + " ["
				+ conn.getOfficialRank() + "]");
		txtViewToAdd.setTextColor(Color.WHITE);
		txtViewToAdd.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		txtViewToAdd.setMinimumWidth(40);
		txtViewToAdd.setGravity(Gravity.LEFT);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		layoutParams.setMargins(3, 3, 3, 3);
		txtViewToAdd.setLayoutParams(layoutParams);
		txtViewToAdd.setSingleLine(true);
		txtViewToAdd.setOnClickListener((RadioTourActivity) context);
		txtViewToAdd.setOnLongClickListener(new DriverViewLongClick(riderNr,
				txtViewToAdd));
		return txtViewToAdd;
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

	public void removeRiderNr(Integer driverNr) {
		group.removeDriverNumber(driverNr);
		LinearLayout temp = getParentLayout(driverNr);
		map.remove(driverNr);
		if (temp != null && !group.isField()) {
			temp.removeView(mapTextView.get(driverNr));
		}
		isdirty = true;
	}

	@Override
	public Date getTime() {
		return group.getHandicapTime();
	}

	@Override
	public void setTime(final Date date, boolean fromDialog) {
		group.updateHandicapTime(date);
		updateAndPersistDriver(date);
		setDeficits();
		if (fromDialog) {
			fragment.syncToDb();
		}
	}

	private void setDeficits() {
		time.setText(StringUtils.getTimeAsString(group.getHandicapTime()));
		lastTime.setText("("
				+ StringUtils.getTimeAsString(group.getLastHandiCap()) + ")");
	}

	private List<RiderStageConnection> updateAndPersistDriver(Date date) {
		RiderStageConnection temp;
		List<RiderStageConnection> modificationAvoider = new LinkedList<RiderStageConnection>();
		for (int i : group.getDriverNumbers()) {
			temp = app.getRiderStage(i);
			try {
				temp.setVirtualDeficit(date);
			} catch (NullPointerException e) {
			}
			modificationAvoider.add(temp);
		}
		return modificationAvoider;
	}

	public void setFragment(DriverGroupFragment fragment) {
		this.fragment = fragment;
	}

	/*
	 * ClickListeners
	 */
	private class DriverViewLongClick implements OnLongClickListener {
		private final int ridernr;
		private final TextView view;

		public DriverViewLongClick(int ridernr, TextView view) {
			this.ridernr = ridernr;
			this.view = view;
		}

		@Override
		public boolean onLongClick(View v) {
			final TextView textView = (TextView) v;
			ClipData data = ClipData.newPlainText(textView.getText(),
					textView.getText());
			SortedSet<Integer> localState = new TreeSet<Integer>();
			localState.add(ridernr);
			v.startDrag(data, new DragShadowBuilder(view), localState, 0);
			return true;
		}
	}

}
