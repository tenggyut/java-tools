package com.tenggyut.math.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tenggyut.common.logging.LogFactory;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * utility class for prime number
 * <p/>
 * Created by tenggyt on 2016/2/2.
 */
public class PrimeNumberUtils {
    private static final Logger LOG = LogFactory.getLogger(PrimeNumberUtils.class);

    private static final List<Integer> PRIME_NUMBER_LIST = ImmutableList.of(
//            30881, 30893, 30911, 30931, 30937, 30941, 30949, 30971, 30977, 30983,
//            31013, 31019, 31033, 31039, 31051, 31063, 31069, 31079, 31081, 31091,
//            31121, 31123, 31139, 31147, 31151, 31153, 31159, 31177, 31181, 31183,
//            31189, 31193, 31219, 31223, 31231, 31237, 31247, 31249, 31253, 31259,
//            31267, 31271, 31277, 31307, 31319, 31321, 31327, 31333, 31337, 31357,
//            31379, 31387, 31391, 31393, 31397, 31469, 31477, 31481, 31489, 31511,
//            31513, 31517, 31531, 31541, 31543, 31547, 31567, 31573, 31583, 31601,
//            31607, 31627, 31643, 31649, 31657, 31663, 31667, 31687, 31699, 31721,
//            31723, 31727, 31729, 31741, 31751, 31769, 31771, 31793, 31799, 31817,
//            31847, 31849, 31859, 31873, 31883, 31891, 31907, 31957, 31963, 31973,
//            31981, 31991, 32003, 32009, 32027, 32029, 32051, 32057, 32059, 32063,
//            32069, 32077, 32083, 32089, 32099, 32117, 32119, 32141, 32143, 32159,
//            32173, 32183, 32189, 32191, 32203, 32213, 32233, 32237, 32251, 32257,
//            32261, 32297, 32299, 32303, 32309, 32321, 32323, 32327, 32341, 32353,
//            32359, 32363, 32369, 32371, 32377, 32381, 32401, 32411, 32413, 32423,
//            32429, 32441, 32443, 32467, 32479, 32491, 32497, 32503, 32507, 32531,
//            32533, 32537, 32561, 32563, 32569, 32573, 32579, 32587, 32603, 32609,
//            32611, 32621, 32633, 32647, 32653, 32687, 32693, 32707, 32713, 32717
            7, 11, 13, 17, 19, 23, 29,
            31, 37, 41, 43, 47, 53, 59, 61,
            67, 71, 73, 79, 83, 89, 97, 101, 103
    );

    private static final Random random = new Random(1);

    public static List<Integer> getRandomPrimeNumbers(int count) {
        Set<Integer> candidates = Sets.newHashSet();
        while (candidates.size() != count) {
            candidates.add(PRIME_NUMBER_LIST.get(random.nextInt(PRIME_NUMBER_LIST.size())));
        }
        return Lists.newArrayList(candidates);
    }

    public static Map<String, Integer> mappingVar2PrimeNumber(List<String> vars) {
        Map<String, Integer> mapping = Maps.newHashMap();
        List<Integer> primeNumbers = PrimeNumberUtils.getRandomPrimeNumbers(vars.size());
        for (int i = 0; i < vars.size(); i++) {
            mapping.put(vars.get(i), primeNumbers.get(i));
        }
        return mapping;
    }
}
