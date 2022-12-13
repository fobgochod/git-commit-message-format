package com.fobgochod.git.commit.constant;

/**
 * GitCommitConstant.java
 *
 * @author fobgochod
 * @date 2022/12/13 2:18
 */
public class GitCommitConstant {

    public static final String DEFAULT_TEMPLATE = """
            $type$($scope$): $subject$
                        
            $body$
                        
            $changes$
            """;
}
