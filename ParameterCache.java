/**
 * 
 */
package org.tinygroup.studio.eclipse.framework.metadata.cache;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.tinygroup.studio.commons.metadata.Parameter;
import org.tinygroup.studio.eclipse.framework.IMetadataConstant;
import org.tinygroup.studio.eclipse.framework.scan.ScanOperationHelper;
import org.tinygroup.studio.eclipse.framework.scan.XMLResourceHandle;
import org.tinygroup.sutdio.metadata.model.TreeCategory;
import org.tinygroup.sutdio.metadata.model.TreeModel;

/**
 * 
 * @ClassName: ParameterCache
 * @author xl xulang21224@hundsun.com
 * @date 2017年12月25日
 *
 */
@SuppressWarnings("restriction")
public class ParameterCache {

	private static Map<String, List<TreeModel>> dataModelList = new HashMap<String, List<TreeModel>>();
	
	private static Map<String, Set<TreeModel>> dataModelSet = new HashMap<String, Set<TreeModel>>();

	private static Map<String, List<IFile>> fileList = new HashMap<String, List<IFile>>();

	public static List<IFile> getIFiles(IProject project) {
		if (fileList.get(project.getName()) == null) {
			reload(project);
		}
		return fileList.get(project.getName());
	}

	public static List<TreeModel> getAllDataModelByProject(IProject project) {
		if (dataModelList.get(project.getName()) == null) {
			reload(project);
		}
		return dataModelList.get(project.getName());
	}
	
	public static Set<TreeModel> getAllRepeatDataModelByProject(IProject project) {
		if (dataModelSet.get(project.getName()) == null) {
			reload(project);
		}
		return dataModelSet.get(project.getName());
	}

	/**
	 * 重新加载缓存
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void reload(IProject project) {
		List<IFile> files = new ArrayList<IFile>();

		XMLResourceHandle handle = new XMLResourceHandle(project, TreeCategory.class) {
			@Override
			protected TreeModel serializableFile(InputStream inputStream, IFile file) {
				files.add(file);
				TreeCategory tree = (TreeCategory) super.serializableFile(inputStream, file);
				tree.setFilePath(file.getLocation().toOSString());
				tree.setFile(file);
				initTreeByFile(tree, file);
				return tree;
			}

			protected TreeModel serializableJarEntryFile(InputStream inputStream, JarEntryFile file) {
				TreeCategory tree = (TreeCategory) super.serializableJarEntryFile(inputStream, file);
				tree.setJarEntry(file);
				initTreeByJarFile(tree, file);
				return tree;
			}

		};
		handle.setProcessAnnotations(new Class[] { TreeCategory.class, Parameter.class });
		List<TreeModel> entities = ScanOperationHelper.scanProject(project, handle,
				IMetadataConstant.PARAMETER_EXTENSION);
		fileList.put(project.getName(), files);
		dataModelList.put(project.getName(), new ArrayList<TreeModel>());
		dataModelSet.put(project.getName(), new HashSet<TreeModel>());

		dataModelList.get(project.getName()).addAll(entities);
		dataModelSet.get(project.getName()).addAll(entities);

	}

	/**
	 * 初始化tree
	 * 
	 * @param root
	 * @param file
	 */
	private static void initTreeByFile(TreeModel root, IFile file) {
		if (!root.isCategory())
			((Parameter) root).setFilePath(file.getLocation().toOSString());
		else {
			for (int i = 0; i < root.getTreeChildren().size(); i++) {
				TreeModel item = root.getTreeChildren().get(i);
				item.setNodeParent(root);
				initTreeByFile(item, file);
			}
		}
	}

	private static void initTreeByJarFile(TreeModel root, JarEntryFile file) {
		if (!root.isCategory())
			((Parameter) root).setJarEntry(file);
		else {
			for (int i = 0; i < root.getTreeChildren().size(); i++) {
				TreeModel item = root.getTreeChildren().get(i);
				item.setNodeParent(root);
				initTreeByJarFile(item, file);
			}
		}
	}

	public static void clear(IProject project) {
		dataModelList = new HashMap<String, List<TreeModel>>();
		dataModelSet = new HashMap<String, Set<TreeModel>>();
		fileList = new HashMap<String, List<IFile>>();
		reload(project);
	}
}
