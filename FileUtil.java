import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Administrator
 *
 */
public class FileUtil {
  
	/**
	 * 根据指定的目录，过滤条件，给它传递一个数据对象，它把数据装进该对象并返回给你
	 * @param result  给方法传一个Map<String, List<File>>对象，它把数据装进去，并把它返回这个对象
	 * @param root 搜索根目录
	 * @param suffix 过滤后缀文件名
	 * @param searchLevel 搜索级数   小于等于-1为搜索所有级， 大于0的按指定的层数进行搜索
	 * @return Map<String, List<File>>
	 */
	public static Map<String, List<File>> getCatalogFiles(Map<String, List<File>> result, String root, String[] suffix, int searchLevel){
		if(result != null && root != null && searchLevel != 0){
			File catalogs = new File(root);
			if(catalogs != null && catalogs.exists() && catalogs.listFiles().length > 0){
				result.put(root, getFilesBySuffix(root, suffix)); //第一层
				List<File> children = getChildCatalogs(root);
				if(children != null){
					for(File f:children){
						if(searchLevel > 0){
							getCatalogFiles(result, f.getAbsolutePath(), suffix, searchLevel - 1); // 进行有限的层搜索
						}else if(searchLevel <= -1){
							getCatalogFiles(result, f.getAbsolutePath(), suffix, searchLevel); //搜索所有级的子目录搜索
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获取root目录下所有文件
	 * @param root
	 * @return
	 */
	public static List<File> getFiles(String root){
		return getFilesBySuffix(root, null);
	}
	
	/**
	 * 根据后缀名查找文件
	 * @param root
	 * @param suffix 如：{".jpg", ".gif"}
	 * @return
	 */
	public static List<File> getFilesBySuffix(String root, String[] suffix) {
		List<File> result = new ArrayList<File>();
		File file = new File(root);
		if (file != null && file.exists() && file.listFiles().length > 0) {
			for (File f : file.listFiles()) {
				if (f.isFile()) {
					if (suffix != null) {
						for (String s : suffix) {
							if (f.getName().endsWith(s)) {
								result.add(f);
							}
						}
					} else {
						result.add(f); // 如果 suffix为空获取所有的文件
					}
				}
			}
			return result;
		}
		return null;
	}
	
	/**
	 * 获取root目录下第一级子目录
	 * @param root
	 * @return
	 */
	public static List<File> getChildCatalogs(String root){
		List<File> result = new ArrayList<File>();
		File file = new File(root);
		if(file != null && file.exists() && file.listFiles().length > 0){
			for(File f:file.listFiles()){
				if(f.isDirectory()){
					result.add(f);
				}
			}
			return result;
		}
		return null;
	}
	
	public static void main(String[] args) {
		Map<String, List<File>> result = new HashMap<String, List<File>>();
		String root = "F:\\相片\\家人";
		result = getCatalogFiles(result, root, new String[]{".jpg"}, 2);
		Iterator<Entry<String, List<File>>> i = result.entrySet().iterator();
		while(i.hasNext()){
			Entry<String, List<File>> e = i.next();
			System.out.println(e.getKey() + ":" + e.getValue().size());
			for(File f:e.getValue()){
				System.out.println(f.getName() + ":" + f.getPath() + ":" + f.getAbsolutePath() + ":" + f.length() * 1d/(1024*1024) + "MB");
			}
		}
	}
}
