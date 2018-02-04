package com.kun.migration.constants;

import com.kun.migration.util.AesUtil;
import org.springframework.stereotype.Component;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/4 10:27
 */
@Component
public enum Style {
    
    
    /**
     * 直接转移
     */
    NORMAL {
        @Override
        public Object format(Object sourceDatum) {
            return sourceDatum;
        }
    },
    
    /**
     * 增加常数，参考
     */
    ADD {
        @Override
        public Object format(Object sourceDatum) {
            if (sourceDatum instanceof Integer)
                return Integer.sum((Integer) sourceDatum, 1);
            return Integer.parseInt((String) sourceDatum) + 1;
        }
    },
    
    /**
     * AES加密
     */
    AES_ENC {
        
        @Override
        public Object format(Object sourceDatum) {
            return AesUtil.encrypt(AesUtil.getAesKey(), (String) sourceDatum);
        }
    },
    
    /**
     * AES解密
     */
    AES_DEC {
        
        @Override
        public Object format(Object sourceDatum) {
            return AesUtil.decrypt(AesUtil.getAesKey(), (String) sourceDatum);
        }
    };
    
    abstract public Object format(Object sourceDatum);
    
}
