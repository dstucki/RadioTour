package ch.hsr.sa.radiotour.technicalservices.listener;

import java.util.TreeSet;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.fragments.DriverGroupFragment;

public class GroupingDragListener implements OnDragListener {
	private TableRow actualLayout = null;
	private final Context ctx;

	public GroupingDragListener(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		final int action = event.getAction();

		switch (action) {
		case DragEvent.ACTION_DRAG_ENTERED:
			if (v instanceof TableRow) {
				v.setBackgroundResource(R.drawable.drag_highlight_border);
				actualLayout = (TableRow) v;
			}
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			if (v instanceof TableRow) {
				v.setBackgroundResource(R.drawable.drag_unhighlight_border);
				actualLayout = null;
			}
		case DragEvent.ACTION_DRAG_ENDED:
			if (v instanceof TableRow) {
				v.setBackgroundResource(R.drawable.drag_unhighlight_border);
			}
		case DragEvent.ACTION_DROP:
			if (v instanceof TableRow) {
				Object clipDataLocalState = event.getLocalState();
				if (clipDataLocalState instanceof TextView) {
					final int riderNr = Integer
							.valueOf(((TextView) clipDataLocalState).getText()
									.toString());
					processDrop(v, event, riderNr);

				} else {

					for (Integer textView : ((TreeSet<Integer>) clipDataLocalState)) {
						processDrop(v, event, textView);
					}
					((RadioTourActivity) ctx).clearCheckedIntegers();
				}
			}
		default:
			break;
		}
		return true;
	}

	public void processDrop(View v, DragEvent event, Integer riderNr) {
		DriverGroupFragment groupFragment = (DriverGroupFragment) ((RadioTourActivity) ctx)
				.getFragmentManager().findFragmentById(R.id.detailFragment);
		handleDuplicates(v, riderNr);
		if (event.getResult() && actualLayout != null && actualLayout == v) {
			if (groupFragment.getField() != (TableRow) v) {
				addTextView(actualLayout, riderNr);
			}
		}
	}

	public void handleDuplicates(View v, Integer riderNr) {
		DriverGroupFragment groupFragment = (DriverGroupFragment) ((RadioTourActivity) ctx)
				.getFragmentManager().findFragmentById(R.id.detailFragment);
		for (TableRow row : groupFragment.getTableRows()) {
			int childcount = row.getChildCount();
			for (int i = 0; i < childcount; i++) {
				View v1 = row.getChildAt(i);
				if (v1 instanceof TextView && v1.getId() == riderNr) {
					row.removeView(v1);
					row.invalidate();
				}
			}
		}
	}

	public void addTextView(TableRow layout, Integer riderNr) {
		handleDuplicates(layout, riderNr);
		final TextView txtViewToAdd = new TextView(ctx);
		txtViewToAdd.setId(riderNr);
		txtViewToAdd.setText(riderNr + "");
		txtViewToAdd.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		txtViewToAdd.setMinimumWidth(40);
		txtViewToAdd.setGravity(Gravity.RIGHT);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(3, 3, 3, 3);
		txtViewToAdd.setBackgroundColor(Color.RED);
		txtViewToAdd.setLayoutParams(layoutParams);

		layout.addView(txtViewToAdd);
		txtViewToAdd.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				final TextView textView = (TextView) v;
				ClipData data = ClipData.newPlainText(textView.getText(),
						textView.getText());
				v.startDrag(data, new DragShadowBuilder(v), v, 0);
				return true;
			}
		});
	}
}
