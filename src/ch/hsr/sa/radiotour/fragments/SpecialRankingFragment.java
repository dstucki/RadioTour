package ch.hsr.sa.radiotour.fragments;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.SpecialRankingListAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Judgement;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.SpecialPointHolder;
import ch.hsr.sa.radiotour.domain.SpecialRanking;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.fragments.controller.SpecialRankingController;

/**
 * 
 * Class to display all infos that are associated with SpecialRankings
 * 
 */
public class SpecialRankingFragment extends Fragment {
	// Adapter for Spinners
	private ArrayAdapter<SpecialRanking> specialAdapter;
	private ArrayAdapter<Judgement> judgementAdapter;

	// Spinners
	private Spinner judgementSpinner;
	private Spinner specialRankingSpinner;

	// Controller
	private SpecialRankingController controller;

	/**
	 * Removes all TextViews that are used for the input of the winning Rider
	 * 
	 * @return Layout from which all TextViews were removed
	 */
	private LinearLayout clearTextViews() {
		LinearLayout llparent = (LinearLayout) getView().findViewById(
				R.id.llayout_rider_set);
		llparent.removeAllViews();
		return llparent;
	}

	/**
	 * Creates and adds the amount of TextViews that are required according to
	 * the actual Selected {@link Judgement} Object. Calls
	 * {@link SpecialRankingFragment#addTextViews(LinearLayout, List)}
	 */
	private void fillJudgementInfo() {
		if (getView() == null) {
			return;
		}
		LinearLayout llparent = clearTextViews();
		List<SpecialPointHolder> listPointHolder = controller
				.getPointHolder(getActualJudgement());
		addTextViews(llparent, listPointHolder);
	}

	/**
	 * Adds the right amount of TextViews to the given layout
	 * 
	 * @param llparent
	 *            layout where the {@link TextView} will be added
	 * @param listPointHolder
	 */
	private void addTextViews(LinearLayout llparent,
			List<SpecialPointHolder> listPointHolder) {
		LinearLayout ll;
		for (int i = 0; i < getActualJudgement().getNrOfWinningRiders(); i++) {
			ll = (LinearLayout) inflate(R.layout.riderset_judgement);
			Rider rider = listPointHolder.get(i).getRider();
			int startNr = rider == null ? 0 : rider.getStartNr();
			((EditText) ll.findViewById(R.id.edtxt_for_number_insert))
					.setText(startNr + "");
			((TextView) ll.findViewById(R.id.txt_rank_in_words))
					.setText((i + 1) + ".");
			llparent.addView(ll);
		}
	}

	/**
	 * HelperMethod where a resource ID to a layout can be passed in. This ID
	 * will be inflated and the view returned
	 * 
	 * @param resId
	 *            layout ID that will be inflated
	 * @return the inflated view
	 */
	private View inflate(int resId) {
		return getActivity().getLayoutInflater().inflate(resId, null);
	}

	/**
	 * Saves the {@link Judgement}. Aborts if illegal numbers are provided or a
	 * number is assigned multiple
	 * 
	 */
	private void saveJudgement() {
		LinearLayout llparent = (LinearLayout) getView().findViewById(
				R.id.llayout_rider_set);
		Set<Integer> tempSet = new HashSet<Integer>();
		Set<Integer> tempSetOld = new HashSet<Integer>();

		List<SpecialPointHolder> listPointHolder = controller
				.getPointHolder(getActualJudgement());

		SpecialPointHolder holder;
		LinearLayout ll;
		TextView error;
		for (int i = 0; i < getActualJudgement().getNrOfWinningRiders(); i++) {
			ll = (LinearLayout) llparent.getChildAt(i);
			error = ((TextView) ll.findViewById(R.id.txt_rank_error_show));
			error.setText("");
			holder = listPointHolder.get(i);
			int temp = 0;
			try {
				temp = getInteger((TextView) ll
						.findViewById(R.id.edtxt_for_number_insert));

			} catch (Exception e) {
				error.setText(getResources().getString(
						R.string.lb_invalidnumber));
				return;
			}
			if (temp != 0 && tempSet.contains(temp)) {
				error.setText(getResources()
						.getString(R.string.lb_doubleassing));
				return;
			}
			tempSet.add(temp);
			tempSetOld.add(holder.getRider() == null ? 0 : holder.getRider()
					.getStartNr());
			holder.setRider(getApp().getRider(temp));
			controller.update(holder);
		}
		tempSet.addAll(tempSetOld);
		controller.calculateBonis(tempSet);
		setVirtualRanking();
	}

	/**
	 * HelperMethod that parses the text from a TextView into an int
	 * 
	 * @param view
	 *            from where the number should be parsed
	 * 
	 * @return number that is written in the TextView
	 */
	private int getInteger(TextView view) {
		return Integer.valueOf(view.getText().toString());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.special_ranking_fragment,
				container, false);
		controller = new SpecialRankingController(this);
		assignClickListeners(view);
		createSpecialSpinner(view);
		createJudgementSpinner(view);
		return view;
	}

	/**
	 * creates the spinner and the adapter for the specialrankings
	 * 
	 * @param view
	 *            where the spinner is found
	 */
	private void createSpecialSpinner(View view) {
		specialRankingSpinner = (Spinner) view
				.findViewById(R.id.spinner_special_ranking);
		specialAdapter = new ArrayAdapter<SpecialRanking>(getActivity(),
				android.R.layout.simple_spinner_item);
		specialAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		specialAdapter.addAll(controller.getAllRankings());
		specialRankingSpinner.setAdapter(specialAdapter);
		specialRankingSpinner
				.setOnItemSelectedListener(new SpecialRankingSelectedListener());
	}

	/**
	 * creates the spinner and the adapter for the judgements
	 * 
	 * @param view
	 *            where the spinner is found
	 */
	private void createJudgementSpinner(View view) {
		judgementSpinner = (Spinner) view.findViewById(R.id.spinner_judgement);
		judgementAdapter = new ArrayAdapter<Judgement>(getActivity(),
				android.R.layout.simple_spinner_item);
		judgementAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		judgementSpinner.setAdapter(judgementAdapter);
		judgementSpinner
				.setOnItemSelectedListener(new JudgementSelectedListener());
	}

	/**
	 * assigns the OnClickListener to the Buttons in the Fragment.
	 * 
	 * @param view
	 *            where all the Buttons were found
	 */
	private void assignClickListeners(View view) {
		view.findViewById(R.id.button_add_new_special_ranking)
				.setOnClickListener(new AddRankingListener());
		view.findViewById(R.id.button_edit_special_ranking).setOnClickListener(
				new EditRankingListener());
		view.findViewById(R.id.button_delete_special_ranking)
				.setOnClickListener(new DeleteRankingListener());
		view.findViewById(R.id.button_save_judgement).setOnClickListener(
				new SaveJudgementListener());
		view.findViewById(R.id.button_delete_judgement).setOnClickListener(
				new DeleteJudgementListener());
		view.findViewById(R.id.button_add_new_judgement).setOnClickListener(
				new AddJudgementListener());
	}

	/**
	 * Method that can be accessed to add or update a {@link SpecialRanking}. It
	 * will add the ranking to the spinner and update or create it in the
	 * Database.
	 * 
	 * @param ranking
	 *            SpecialRanking to be added to the spinner
	 */
	public void setSpecialRankingFromDialog(SpecialRanking ranking) {
		controller.createUpdateRanking(ranking);
		specialAdapter.clear();
		specialAdapter.addAll(controller.getAllRankings());
	}

	/**
	 * clears {@link Judgement} from adapter and read them new from Database
	 */
	private void fillJudgements() {
		judgementAdapter.clear();
		if (getSpecialRanking() != null) {
			judgementAdapter.addAll(controller.getJudgements(
					getSpecialRanking(), getStage()));
			judgementSpinner.setSelection(judgementAdapter.getCount());
			setVirtualRanking();
		}
	}

	/**
	 * update the listview that shows the ranking of the actual selected
	 * {@link SpecialRanking}
	 */
	private void setVirtualRanking() {
		ListView lv = (ListView) getView().findViewById(
				R.id.listview_place_for_special_ranking);
		if (lv.getHeaderViewsCount() == 0) {
			lv.addHeaderView(inflate(R.layout.textview_ranking_special_ranking));
		}
		lv.setAdapter(new SpecialRankingListAdapter(getActivity(), controller
				.getVirtualMap(getSpecialRanking())));
	}

	/**
	 * Method that can be accessed to add a {@link Judgement}. It will add the
	 * Judgement to the spinner and create it in the Database.
	 * 
	 * @param judgement
	 *            Judgement to be added to the spinner and database
	 */

	public void newJudgementCreated(Judgement judgement) {
		controller.createJudgement(judgement);
		judgementAdapter.add(judgement);
		fillJudgements();
		judgementSpinner.setSelection(judgementAdapter.getCount() - 1);
	}

	/**
	 * HelperMethod to avoid having a field in the class
	 * 
	 * @return the actual Selected {@link SpecialRanking}
	 */
	private SpecialRanking getSpecialRanking() {
		return (SpecialRanking) specialRankingSpinner.getSelectedItem();
	}

	/**
	 * HelperMethod to avoid having a field in the class
	 * 
	 * @return the actual Selected {@link Judgement}
	 */
	private Judgement getActualJudgement() {
		return (Judgement) judgementSpinner.getSelectedItem();
	}

	/**
	 * HelperMethod to avoid having a field in the class
	 * 
	 * @return the actual Selected {@link Stage}
	 */
	private Stage getStage() {
		return getApp().getActualSelectedStage();
	}

	/**
	 * HelperMethod to avoid having a field in the class
	 * 
	 * @return the Application class {@link RadioTour}
	 */
	private RadioTour getApp() {
		return (RadioTour) getActivity().getApplication();
	}

	/**
	 * HelperMethod to avoid having a field in the class
	 * 
	 * @return the activity {@link RadioTourActivity}
	 */

	private RadioTourActivity getAct() {
		return (RadioTourActivity) getActivity();
	}

	// Begin Spinner Listeners

	/**
	 * abstract {@link OnItemSelectedListener} where onNothingSelected is
	 * already implemented
	 * 
	 */
	private abstract class SpecialSelectedListener implements
			OnItemSelectedListener {
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			clearTextViews();
		}
	}

	/**
	 * Class to handle selection changes where the items in the Spinner are of
	 * the type {@link SpecialRanking}. Extends {@link SpecialSelectedListener}
	 * 
	 */
	private class SpecialRankingSelectedListener extends
			SpecialSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			clearTextViews();
			fillJudgements();
		}

	};

	/**
	 * Class to handle selection changes where the items in the Spinner are of
	 * the type {@link Judgement}. Extends {@link SpecialSelectedListener}
	 * 
	 */
	private class JudgementSelectedListener extends SpecialSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			fillJudgementInfo();
		}
	};

	// End Spinner Listeners

	// Begin OnClickListeners
	/**
	 * ClickListener to handle adding of a {@link Judgement}
	 * 
	 */
	private class AddJudgementListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (getSpecialRanking() == null) {
				return;
			}
			getAct().showTextViewDialog(
					SpecialRankingFragment.this,
					controller.generateJudgement(getStage(),
							getSpecialRanking()));
		}

	}

	/**
	 * ClickListener to handle deletion of a {@link Judgement}
	 * 
	 */
	private class DeleteJudgementListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			final Judgement temp = getActualJudgement();
			if (temp != null) {
				judgementAdapter.remove(getActualJudgement());
				controller.delete(temp);
				fillJudgements();
			}
		}

	}

	/**
	 * ClickListener to handle saving of a {@link Judgement}
	 * 
	 */
	private class SaveJudgementListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.i(getClass().getSimpleName(), "hee");
			saveJudgement();
		}
	}

	/**
	 * ClickListener to handle adding of a {@link SpecialRanking}
	 * 
	 */
	private class AddRankingListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			getAct().showSpecialRankingDialog(SpecialRankingFragment.this, null);
		}
	}

	/**
	 * ClickListener to handle edit of a {@link SpecialRanking}
	 * 
	 */
	private class EditRankingListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			getAct().showSpecialRankingDialog(SpecialRankingFragment.this,
					getSpecialRanking());
		}
	}

	/**
	 * ClickListener to handle deletion of a {@link SpecialRanking}
	 * 
	 */
	private class DeleteRankingListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			final SpecialRanking temp = getSpecialRanking();
			if (temp != null) {
				judgementAdapter.clear();
				specialAdapter.remove(temp);
				controller.delete(temp);
				setVirtualRanking();
			}
		}
	}

}
