package ch.hsr.sa.radiotour.technicalservices.listener;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.fragments.DriverGroupFragment;
import ch.hsr.sa.radiotour.views.GroupTableRow;

public class GroupingDragListener implements OnDragListener {
	private TableRow actualLayout = null;
	private final Context ctx;
	private final DriverGroupFragment groupFragment;

	public GroupingDragListener(Context ctx, DriverGroupFragment groupFragment) {
		this.ctx = ctx;
		this.groupFragment = groupFragment;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_ENTERED:
			if (v instanceof TableRow) {
				v.setBackgroundResource(R.drawable.drag_highlight_border);
				actualLayout = (TableRow) v;
			}
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			if (v instanceof TableRow) {
				handleBorder((TableRow) v);
				actualLayout = null;
			}
		case DragEvent.ACTION_DRAG_ENDED:
			if (v instanceof TableRow) {
				handleBorder((TableRow) v);
			}
		case DragEvent.ACTION_DROP:
			handleBorder((TableRow) v);
			if (event.getResult()) {
				if (v instanceof TableRow && v == actualLayout) {
					((RadioTourActivity) ctx).onRowLayoutClick(actualLayout,
							event.getLocalState());
				}
				v.invalidate();
			}
		default:
			break;
		}
		return true;
	}

	public void handleBorder(TableRow v) {
		if (v != null && v.getChildCount() >= 1) {
			v.setBackgroundResource(R.drawable.drag_unhighlight_border);
			if (!(v instanceof GroupTableRow)) {
				v.setBackgroundResource(R.drawable.create_new_group);
			}
		}
	}

	public void handleDuplicates(Integer riderNr) {
		for (TableRow row : groupFragment.getTableRows()) {
			int childcount = row.getChildCount();
			for (int i = 2; i < childcount; i++) {
				View v1 = row.getChildAt(i);
				if (v1 instanceof TextView
						&& Integer
								.valueOf(((TextView) v1).getText().toString()) == riderNr) {
					((GroupTableRow) row).removeView(v1);
					row.invalidate();
				}
			}
		}
	}

	public void addTextView(GroupTableRow layout, Integer riderNr) {
		handleDuplicates(riderNr);
		layout.addRider(riderNr);
	}
}
