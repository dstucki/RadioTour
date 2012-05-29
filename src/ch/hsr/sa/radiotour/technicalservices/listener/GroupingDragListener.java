package ch.hsr.sa.radiotour.technicalservices.listener;

import java.util.TreeSet;

import android.content.Context;
import android.graphics.Typeface;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.fragments.RiderPickerFragment;
import ch.hsr.sa.radiotour.views.GroupTableRow;

public class GroupingDragListener implements OnDragListener {
	private View actualLayout = null;
	private final Context ctx;
	private final RiderPickerFragment pickerFragment;

	public GroupingDragListener(Context ctx, RiderPickerFragment pickerFragment) {
		this.ctx = ctx;
		this.pickerFragment = pickerFragment;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_ENTERED:
			if (v instanceof TextView) {
				((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
			} else {
				v.setBackgroundResource(R.drawable.drag_highlight_border);
			}
			actualLayout = v;
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			handleBorder(v);
			actualLayout = null;
		case DragEvent.ACTION_DRAG_ENDED:
			handleBorder(v);
		case DragEvent.ACTION_DROP:
			handleBorder(v);
			if (event.getResult() && v == actualLayout) {
				((RadioTourActivity) ctx).onRowLayoutClick(actualLayout,
						(TreeSet<Integer>) event.getLocalState());
				pickerFragment.getAdapter().notifyDataSetChanged();
			}
		default:
			break;
		}
		return true;
	}

	public void handleBorder(View v) {
		if (v != null) {
			if (v instanceof TextView) {
				((TextView) v).setTypeface(Typeface.DEFAULT);
				return;
			}
			v.setBackgroundResource(R.drawable.drag_unhighlight_border);
			if (!(v instanceof GroupTableRow)) {
				v.setBackgroundResource(R.drawable.create_new_group);
			}
		}
	}
}
