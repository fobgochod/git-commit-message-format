package com.fobgochod.git.commit.constant;

/**
 *  GitCommitConstant.java
 *
 * @author fobgochod
 * @date 2022/12/13 2:18
 */
public class GitCommitConstant {
    public static final String DEFAULT_TEMPLATE = "#if($type)${type}#end#if($scope)(${scope})#end: #if($subject)${subject}#end\n" +
                                                  "#if($body)${newline}${newline}${body}#end\n" +
                                                  "#if($changes)${newline}${newline}BREAKING CHANGE: ${changes}#end\n" +
                                                  "#if($closes)${newline}${newline}Closes ${closes}#end\n";
}
