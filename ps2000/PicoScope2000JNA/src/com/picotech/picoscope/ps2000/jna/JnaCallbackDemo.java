package com.picotech.picoscope.ps2000.jna;



	import java.util.Arrays;
	import java.util.Comparator;

	import com.sun.jna.Callback;
	import com.sun.jna.Function;
	import com.sun.jna.IntegerType;
	import com.sun.jna.Library;
	import com.sun.jna.Native;
	import com.sun.jna.NativeLibrary;
	import com.sun.jna.Pointer;
	import com.sun.jna.Union;
	import com.sun.jna.WString;
	import com.sun.jna.ptr.IntByReference;
	import com.sun.jna.ptr.PointerByReference;

	public class JnaCallbackDemo {
	    private static final NativeLibrary libind = NativeLibrary.getInstance("indirect");
	    private static final Function strcmp = libind.getFunction("strcmp_ind");
	    private static final Function strcasecmp = libind.getFunction("strcasecmp_ind");
	    private static final Function wcscmp = libind.getFunction("wcscmp_ind");
	    private static final Function wcscasecmp = libind.getFunction("wcscasecmp_ind");
	    private static final ILibC libc = (ILibC) Native.loadLibrary("c", ILibC.class);

	    // Shamelessly copied from Structure.FFIType.
	    public static class size_t extends IntegerType {
	        public size_t() { this(0); }
	        public size_t(long value) { super(Native.SIZE_T_SIZE, value); }
	    }

	    private interface ILibC extends Library {
	        /*
	         * Alas, direct native methods do not support String[] as
	         * argument type, so this has to be an indirect method.
	         */
	        <T> void qsort(T[] base, size_t nel, size_t width, QsortComparator<T> compar);
	    }

	    private static class LibC {
	        static {
	            Native.register("c");
	        }

	        /*
	         * Direct native methods do support int[], so prefer to use
	         * that. Also a good demonstration of how to use both direct
	         * and indirect styles.
	         */
	        static native void qsort(int[] base, size_t nel, size_t width, IntQsortComparator compar);
	    }

	    private interface QsortComparator<T> extends Callback, Comparator<T> {
	        T dereference(PointerByReference ptr);

	        default int callback(PointerByReference lhs, PointerByReference rhs) {
	            return compare(dereference(lhs), dereference(rhs));
	        }
	    }

	    private interface StringQsortComparator extends QsortComparator<String> {
	        @Override
	        default String dereference(PointerByReference ptr) {
	            return ptr.getValue().getString(0);
	        }

	        /*
	         * XXX: JNA actually can't find the callback method if it's in
	         * a superinterface, so we must actually redefine it here. Sigh.
	         */
	        @Override
	        default int callback(PointerByReference lhs, PointerByReference rhs) {
	            return QsortComparator.super.callback(lhs, rhs);
	        }
	    }

	    private interface WStringQsortComparator extends QsortComparator<WString> {
	        @Override
	        default WString dereference(PointerByReference ptr) {
	            return new WString(ptr.getValue().getWideString(0));
	        }

	        @Override
	        default int callback(PointerByReference lhs, PointerByReference rhs) {
	            return QsortComparator.super.callback(lhs, rhs);
	        }
	    }

	    private interface IntQsortComparator extends Callback {
	        int compare(int lhs, int rhs);

	        default int callback(IntByReference lhs, IntByReference rhs) {
	            return compare(lhs.getValue(), rhs.getValue());
	        }
	    }

	    private enum StringComparators implements StringQsortComparator {
	        STRCMP {
	            @Override
	            public int compare(String lhs, String rhs) {
	                return lhs.compareTo(rhs);
	            }
	        },

	        STRCASECMP {
	            @Override
	            public int compare(String lhs, String rhs) {
	                return lhs.compareToIgnoreCase(rhs);
	            }
	        };
	    }

	    private enum WStringComparators implements WStringQsortComparator {
	        WCSCMP {
	            @Override
	            public int compare(WString lhs, WString rhs) {
	                return lhs.compareTo(rhs);
	            }
	        },

	        WCSCASECMP {
	            @Override
	            public int compare(WString lhs, WString rhs) {
	                return lhs.toString().compareToIgnoreCase(rhs.toString());
	            }
	        }
	    }

	    private enum IntComparators implements IntQsortComparator {
	        NATURAL {
	            @Override
	            public int compare(int lhs, int rhs) {
	                return Integer.compare(lhs, rhs);
	            }
	        },

	        REVERSE {
	            @Override
	            public int compare(int lhs, int rhs) {
	                return Integer.compare(rhs, lhs);
	            }
	        }
	    }

	    public static class ComparatorUnion<T> extends Union {
	        public Pointer ptr;
	        public QsortComparator<T> callback;

	        public ComparatorUnion(Pointer ptr) {
	            this.ptr = ptr;
	            setType("ptr");
	            write();
	            setType("callback");
	            read();
	        }
	    }

	    private static <T> void sortAndShow(T[] elements, QsortComparator<T> comparator) {
	        libc.qsort(elements, new size_t(elements.length), new size_t(Native.POINTER_SIZE), comparator);
	        System.out.printf("%s: %s%n", comparator, Arrays.toString(elements));
	    }

	    private static void sortAndShow(int[] elements, IntQsortComparator comparator) {
	        LibC.qsort(elements, new size_t(elements.length), new size_t(4), comparator);
	        System.out.printf("%s: %s%n", comparator, Arrays.toString(elements));
	    }

	    public static void main(String[] args) {
	        String[] strings = {"foo", "Bar", "baz", "Qux"};
	        sortAndShow(strings, StringComparators.STRCMP);
	        sortAndShow(strings, StringComparators.STRCASECMP);
	        sortAndShow(strings, (StringQsortComparator) String::compareTo);
	        sortAndShow(strings, (StringQsortComparator) String::compareToIgnoreCase);
	        sortAndShow(strings, new ComparatorUnion<>(strcmp).callback);
	        sortAndShow(strings, new ComparatorUnion<>(strcasecmp).callback);

	        WString[] wstrings = Arrays.stream(strings).map(WString::new).toArray(WString[]::new);
	        sortAndShow(wstrings, WStringComparators.WCSCMP);
	        sortAndShow(wstrings, WStringComparators.WCSCASECMP);
	        sortAndShow(wstrings, (WStringQsortComparator) WString::compareTo);
	        sortAndShow(wstrings, (WStringQsortComparator) (lhs, rhs) ->
	                lhs.toString().compareToIgnoreCase(rhs.toString()));
	        sortAndShow(wstrings, new ComparatorUnion<>(wcscmp).callback);
	        sortAndShow(wstrings, new ComparatorUnion<>(wcscasecmp).callback);

	        int[] ints = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
	        sortAndShow(ints, IntComparators.NATURAL);
	        sortAndShow(ints, IntComparators.REVERSE);
	        sortAndShow(ints, Integer::compare);
	    }
	}

