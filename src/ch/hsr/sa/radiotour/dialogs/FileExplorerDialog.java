package ch.hsr.sa.radiotour.dialogs;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.fragments.AdminFragment;

/**
 * The Class FileExplorerDialog that is used to browse file content on the
 * tablet
 */
public class FileExplorerDialog extends DialogFragment {

	private List<String> item = null;
	private List<String> path = null;

	private final String root = "/";
	private TextView breadCrumb;
	private final AdminFragment fragment;
	private View v;
	private ListView lv;

	private final OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View arg1,
				int position, long id) {
			final File file = new File(path.get(position));

			if (file.isDirectory()) {
				if (file.canRead()) {
					getDir(path.get(position));
				}
			} else {
				fragment.setImportFile(file);
				dismiss();
			}
		}
	};

	public FileExplorerDialog(AdminFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fileexplorer, container, false);
		getDialog().setTitle(
				getResources().getString(R.string.hd_getimportpath));
		breadCrumb = (TextView) v.findViewById(R.id.path);
		lv = (ListView) v.findViewById(R.id.list_file_explorer);
		lv.setOnItemClickListener(itemListener);
		getDir(root);
		return v;
	}

	private void getDir(String dirPath) {
		breadCrumb.setText("Location: " + dirPath);

		item = new ArrayList<String>();
		path = new ArrayList<String>();

		File f = new File(dirPath);
		File[] files = f.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				List<String> list = new ArrayList<String>();
				list.add("sdcard");
				list.add("usbdrive");

				final boolean onRoot = pathname.getParent().equals(root);

				final boolean sdcardOrUsbdrive = list.contains(pathname
						.getName());

				final boolean csvSuffix = pathname.getName().indexOf(".csv") != -1;

				final boolean isDirectory = pathname.isDirectory();

				return (isDirectory && (!onRoot || sdcardOrUsbdrive))
						|| csvSuffix;
			}
		});

		if (!dirPath.equals(root)) {
			item.add(root);
			path.add(root);

			item.add("../");
			path.add(f.getParent());
		}

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			path.add(file.getPath());
			if (file.isDirectory())
				item.add(file.getName() + "/");
			else {
				item.add(file.getName());
			}
		}

		ArrayAdapter<String> fileList = new ArrayAdapter<String>(getActivity(),
				R.layout.fileexplorer_row, item);
		lv.setAdapter(fileList);
	}
}