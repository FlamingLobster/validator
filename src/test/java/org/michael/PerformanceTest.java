package org.michael;

@SuppressWarnings("unused")
class PerformanceTest {
    // I would not bother with performance tests before pushing this out. Unless there are some specific targets we are
    // trying to hit and there are concerns that those target can't be hit (I have no concerns).

    // The bottleneck is likely going to be IO due
    // to the file writer. Unless the output format is changed to be more capable. There's probably a no need to performance test.
    // I.e. the worst algorithms are probably still bottle necked by IO. The most obvious example I can think of is that
    // Firefox by default will race the cache and a request, this is because cache is slower 10% of the time due to IO
    // in the real world. Other aspects of computing has so far outpaced IO that the wasted CPU cycles are not a cost, the
    // CPU will be idle so why not. This isn't a perfect example as time to first byte is likely what Firefox cares about while
    // this program wants high throughput. The example is just an illustration that IO is a serious concern for performance.

    // Also consider that we will have real world examples to learn about performance. Suppose the end user is putting in files
    // in excessive of billions of rows. We can see how slow it will be. But theoretically the performance boundaries of
    // file writer is well known (I couldn't find any readily available benchmarks, but I didn't spend a lot of time looking)
    // so there shouldn't be any surprises here.

    // Due to the use of files. I would not expect there are a lot of gains that can be made using multi threading.
    // Things become easier if we don't care about ordering as different threads can dump results into the file as
    // they become available. I would guess an excessively large sized buffer per thread that trivializes the time it
    // takes to acquire a file handle is desirable but this will have to be performance tested to be sure.

    // If higher performance is needed, I would seriously consider not enforcing ordering if possible. Enforcing order
    // becomes increasingly undesirable for two reason: recombining is extra work and involves waiting for a block of
    // results to complete before dumping to file; extra code that has to be verified and maintained.
}