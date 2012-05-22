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
import ch.hsr.sa.radiotour.fragments.interfaces.TimePickerIF;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;
import ch.hsr.sa.radiotour.utils.StringUtils;

import com.j256.ormlite.dao.RuntimeExceptionDao;

public class GroupTableRow extends TableRow implements TimePickerIF {
	private TextView description;
	private TextView time, lastTime;
	private final Map<Integer, LinearLayout> map = new HashMap<Integer, LinearLayout>();
	private final Map<Integer, TextView> mapTextView = new HashMap<Integer, TextView>();
	private LinearLayout odd, even;
	private RuntimeExceptionDao<RiderStageConnection, Integer> riderStageDao;
	private RadioTour app;

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
		DatabaseHelper helper = ((RadioTourActivity) context).getHelper();
		riderStageDao = helper.getRiderStageDao();
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
				for (int i = 0; i < odd.getChildCount(); i++) {
					odd.getChildAt(i).performClick();
				}
				for (int i = 0; i < even.getChildCount(); i++) {
					even.getChildAt(i).performClick();
				}
			}
		});

		description.setTextSize(30);
		time.setTextSize(25);
		lastTime.setTextSize(18);

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

	public void rearrangeTextViews() {
		if (group.isField()) {
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
		riderStageDao.createOrUpdate(conn);
		if (!group.isField()) {
			final TextView txtViewToAdd = new TextView(context);
			txtViewToAdd.setId(riderNr);
			txtViewToAdd.setText(conn.getRider().toString() + " ["
					+ conn.getOfficialRank() + "]");
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

			txtViewToAdd.setSingleLine(true);

			temp.addView(txtViewToAdd);
			map.put(riderNr, temp);
			mapTextView.put(riderNr, txtViewToAdd);
			invalidate();
			txtViewToAdd.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					final TextView textView = (TextView) v;
					ClipData data = ClipData.newPlainText(textView.getText(),
							textView.getText());
					SortedSet<Integer> localState = new TreeSet<Integer>();
					localState.add(riderNr);
					v.startDrag(data, new DragShadowBuilder(txtViewToAdd),
							localState, 0);
					return true;
				}
			});
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

	public void removeRiderNr(Integer driverNr) {
		group.removeDriverNumber(driverNr);
		RiderStageConnection conn = app.getRiderStage(driverNr);
		conn.setVirtualDeficit(new Date(0, 0, 0, 0, 0, 0));
		LinearLayout temp = getParentLayout(driverNr);
		map.remove(driverNr);
		riderStageDao.update(conn);
		if (temp != null && !group.isField()) {
			for (int i = 0; i < temp.getChildCount(); i++) {
				final String textViewString = ((TextView) temp.getChildAt(i))
						.getText().toString();
				if (textViewString.equals(conn.getRider().toString() + " ["
						+ conn.getOfficialRank() + "]")) {
					temp.removeViewAt(i);
				}
			}
		}

	}

	@Override
	public Date getTime() {
		return group.getHandicapTime();
	}

	@Override
	public void setTime(final Date date) {
		group.updateHandicapTime(date);

		final List<RiderStageConnection> listToUpdate = updateAndPersistDriver(date);
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (RiderStageConnection conn : listToUpdate) {
					riderStageDao.update(conn);
				}
			}
		}, "WriteToDBThread").start();

		setDeficits();

	}

	private void setDeficits() {
		time.setText(StringUtils.getTimeAsString(group.getHandicapTime()));
		lastTime.setText("("
				+ StringUtils.getTimeAsString(group.getLastHandiCap()) + ")");
	}

	public List<RiderStageConnection> updateAndPersistDriver(Date date) {
		RiderStageConnection temp;
		List<RiderStageConnection> modificationAvoider = new LinkedList<RiderStageConnection>();
		for (int i : group.getDriverNumbers()) {
			temp = app.getRiderStage(i);
			temp.setVirtualDeficit(date);
			modificationAvoider.add(temp);
		}
		return modificationAvoider;
	}
}
