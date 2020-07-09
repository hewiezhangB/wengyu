package com.hundsun.gaps.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.ide.IDE.SharedImages;
import org.tinygroup.studio.commons.metadata.Parameter;
import org.tinygroup.studio.eclipse.framework.metadata.cache.ParameterCache;
import org.tinygroup.studio.eclipse.metadata.ui.Activator;
import org.tinygroup.sutdio.metadata.model.TreeCategory;
import org.tinygroup.sutdio.metadata.model.TreeModel;
/**
 * @author hspcadmin
 * @update hewie 2020年6月10日 16:25:48
 * */
@SuppressWarnings("restriction")
public class ImportParameterWizardPage extends WizardPage {
	
	public static final String ROOT = "root";

	private FilteredTree viewer;

	private IFile selectedFile;
	
	List<Object> listCate = new ArrayList<Object>();

	@SuppressWarnings("unused")
	private TreeModel selectModel;

	protected ImportParameterWizardPage(String pageName, IFile selectedFile, TreeModel selectModel) {
		super(pageName);
		this.selectedFile = selectedFile;
		this.selectModel = selectModel;
		setTitle("选择要导入的参数");
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));

		viewer = new FilteredTree(composite, SWT.BORDER | SWT.MULTI | SWT.VIRTUAL, new PatternFilter() {

			@Override
			public void setPattern(String patternString) {
				if (!StringUtils.isBlank(patternString)){
					patternString = "**" + patternString + "**";
				}
				super.setPattern(patternString);
			}

		}, true) {
			@Override
			protected TreeViewer doCreateTreeViewer(Composite parent, int style) {
				return new ContainerCheckedTreeViewer(parent, style);
			}
		};
//		List<TreeModel> list = ParameterCache.getAllDataModelByProject(selectedFile.getProject());
		Set<TreeModel> list = ParameterCache.getAllRepeatDataModelByProject(selectedFile.getProject());
//		if(viewer.getViewer().getInput() != null){
//			List<Object> treeList = new ArrayList<Object>();
//			Object obj = viewer.getViewer().getInput();
//			treeList.add(obj);
//			for (Object object : treeList) {
//				System.out.println(object);
//			}
//			list.removeAll(treeList);
//		}
		viewer.getViewer().setContentProvider(getContentProvider());
		viewer.getViewer().setLabelProvider(getLabelProvider());
		try {
			viewer.getViewer().setInput(list);
		} catch (Exception e) {
			ParameterCache.reload(selectedFile.getProject());
			list = ParameterCache.getAllRepeatDataModelByProject(selectedFile.getProject());
//			if(viewer.getViewer().getInput() != null){
//				List<Object> treeList = new ArrayList<Object>();
//				Object obj = viewer.getViewer().getInput();
//				treeList.add(obj);
//				for (Object object : treeList) {
//					System.out.println(object);
//				}
//				list.removeAll(treeList);
//			}
			viewer.getViewer().setContentProvider(getContentProvider());
			viewer.getViewer().setLabelProvider(getLabelProvider());
			viewer.getViewer().setInput(list);
		}
		viewer.getViewer().expandToLevel(2);
		setControl(parent);
		validate();
		((ContainerCheckedTreeViewer) viewer.getViewer()).addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				validate();
			}
		});
	}

	private void validate() {
		setMessage(null);
		setErrorMessage(null);
		setPageComplete(true);
		Object[] select = ((ContainerCheckedTreeViewer) viewer.getViewer()).getCheckedElements();
		if (select == null || select.length == 0) {
			setErrorMessage("没有选择任何参数！");
			setPageComplete(false);
		}
	}

	public Object[] getSelect() {
		return ((ContainerCheckedTreeViewer) viewer.getViewer()).getCheckedElements();
	}

	public IBaseLabelProvider getLabelProvider() {
		return new ILabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener listener) {

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {

				return false;
			}

			@Override
			public void dispose() {

			}

			@Override
			public void addListener(ILabelProviderListener listener) {

			}

			@Override
			public String getText(Object element) {
				TreeModel model = (TreeModel) element;
				if (model instanceof TreeCategory && ROOT.equals(((TreeCategory) model).getName())) {
					Object file = ((TreeCategory) model).getFile();
					if (file == null){
						file = ((TreeCategory) model).getJarEntry();
					}
					listCate.add(file);
					if (file instanceof IFile) {
						IFile ifile = (IFile) file;
						return ifile.getProject().getName() + "(" + ifile.getName() + ")";
					} else if (file instanceof JarEntryFile) {
						JarEntryFile jarEntryFile = (JarEntryFile) file;
						return jarEntryFile.getPackageFragmentRoot().getElementName() + "(" + jarEntryFile.getName()
								+ ")";
					}
				}
				if (model != null) {
					if (model instanceof Parameter) {
						Parameter parameter = (Parameter) model;
						return model.getItemName() + "[" + parameter.getKey() + "]";

					} else {
						return model.getItemName();
					}
				}

				return "ERROR";

			}

			@Override
			public Image getImage(Object element) {
				TreeModel model = (TreeModel) element;
				if (model instanceof TreeCategory && ROOT.equals(((TreeCategory) model).getName())) {
					Object file = ((TreeCategory) model).getFile();
					if (file == null){
						file = ((TreeCategory) model).getJarEntry();
					}
					if (file instanceof IFile) {
						return PlatformUI.getWorkbench().getSharedImages().getImage(SharedImages.IMG_OBJ_PROJECT);
					} else if (file instanceof JarEntryFile) {
						return JavaPluginImages.get(ISharedImages.IMG_OBJS_JAR);
					}
				}
				if (element instanceof TreeCategory) {
					return Activator.getImage("show_group.png");
				}
				return Activator.getImage("parameter_item.png");

			}
		};
	}

	protected ITreeContentProvider getContentProvider() {
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public boolean hasChildren(Object element) {
				TreeModel treeModel = (TreeModel) element;
				if (treeModel != null && treeModel.getTreeChildren() != null && treeModel.getTreeChildren().size() > 0){
					return true;
				} else {
					return false;
				}
			}

			@Override
			public Object getParent(Object element) {

				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Set) {
					return ((Set) inputElement).toArray();
				}
				TreeModel treeModel = (TreeModel) inputElement;
				if (treeModel != null){
					return treeModel.getTreeChildren().toArray();
				} else {
					return new Object[0];
				}
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				TreeModel treeModel = (TreeModel) parentElement;
				if (treeModel != null && treeModel.getTreeChildren() != null && treeModel instanceof TreeCategory){
					return treeModel.getTreeChildren().toArray();
				}
				return null;
			}
		};
	}

}
