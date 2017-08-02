package cn.nju.seg.atg.BEParse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndexFileLocation;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.internal.core.parser.IMacroDictionary;
import org.eclipse.cdt.internal.core.parser.scanner.InternalFileContent;
import org.eclipse.cdt.internal.core.parser.scanner.InternalFileContentProvider;
import org.eclipse.core.runtime.CoreException;


public class CDTAstUtil {
	
	private static String AbsolutePath=new File("").getAbsolutePath().toString();
	private static String filePath=AbsolutePath+"\\binaryExpression.c";;
	
	public CDTAstUtil()
	{
		
	}
	public CDTAstUtil(String filePath)
	{
		this.filePath=filePath;
	}
	//得到C++源码的编译单元
	public IASTTranslationUnit getIASTTranslationUnit()
	{ 
		
		 IParserLogService log = new DefaultLogService(); 
		 FileContent ct = FileContent.createForExternalFileLocation(this.filePath); 
		 IASTTranslationUnit astUnit=null;
		 try {
			 	//C++源码的编译单元
				astUnit=GPPLanguage.getDefault().getASTTranslationUnit(ct,new Scanner(),null, null, 0, log); 
		  } catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  }
	  return astUnit; 
	} 
	
	public String getAbsolutePath() {
		return AbsolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		AbsolutePath = absolutePath;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	private static class FileProvider extends InternalFileContentProvider 
	 {
		public InternalFileContent getContentForInclusion(String arg0) {
			//return (InternalFileContent) InternalFileContent.createForExternalFileLocation(arg0);
			return null;
		}
		@Override
		public InternalFileContent getContentForInclusion(IIndexFileLocation arg0, String arg1) {
			return null;
		}
		@Override
		public InternalFileContent getContentForInclusion(String filePath,
				IMacroDictionary macroDictionary) {
			// TODO Auto-generated method stub
			return null;
		} 
	 }
	 private static class Scanner implements IScannerInfo 
	 { 
		  @Override 
		  public Map<String, String> getDefinedSymbols() { 
		   Map<String, String> hm = new HashMap<String, String>(); 
		   return hm; 
		  }   
		   
		  @Override 
		  public String[] getIncludePaths() { 
		   return  new String[]{AbsolutePath}; 
		  } 
	 } 
}
