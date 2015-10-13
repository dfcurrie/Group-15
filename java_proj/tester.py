import time
import sys
from subprocess import call


class testCase():
   

    def __init__(self, pid, planes, wait_time, wait_after, total_time, possible, expected_fuel,
            expected_land_1, expected_land_2, time_unit):
        self.time_unit = time_unit
        self.pid = pid
        self.case_name = "case"+str(self.pid)
        self.planes = planes
        self.wait_time = wait_time
        self.wait_after = wait_after
        self.total_time = total_time
        self.possible = possible
        self.expected_fuel = expected_fuel
        self.expected_land = {"normal": expected_land_1, "alternate": expected_land_2}
        
        self.output_possible = True
        self.output_fuel = dict() # keys are plane ids 
        self.output_land = dict() # keys are plane ids

    def __str__(self):
        res = 'echo "CASE '+ str(self.pid) + ':"\n'
        for plane, wait in zip(self.planes, self.wait_time):
            res += 'sleep ' + str(wait * self.time_unit) + '\n'
            res += 'echo "' + str(plane).translate(None, " ") + '"\n'
        res += 'echo ""\n'
        res += 'sleep ' + str(self.wait_after * self.time_unit) + '\n'
        res += 'echo "END"'
        return res

    def execute(self):
        fname = self.case_name + ".sh"
        with open(fname, "w") as f:
            f.write(str(self))
        call(["rm", "-f", "output.txt"])
        call(["chmod", "+x", fname])
        call(["./tester.sh", "./"+fname, str(20*self.time_unit)])

    def parse_output(self, opath):
        with open(opath) as f:
            line = f.readline()
            if line.find('IMPOSSIBLE') != -1:
                self.output_possible = False
            else: 
                line = f.readline()
                while len(line) > 2:
                    if line.find('C') == -1:
                        tokens = line.split()
                        self.output_fuel[int(tokens[0])] = int(tokens[5])
                        self.output_land[int(tokens[0])] = int(tokens[3])
                        line = f.readline()

    def check_output(self):
        res = ""
        res += self.case_name + "\n"
        if self.possible != self.output_possible:
            res += "Error in possibility" + "\n"
        elif not self.possible:
            res += "Correct IMPOSSIBLE" + "\n"
        else:
            diff_f = list()
            for pid, fuel in enumerate(self.expected_fuel):
                _, _, rate, _, _, _ = self.planes[pid]
                pid += 1
                if pid not in self.output_fuel:
                    res += "output size mismatch!\n"
                    res += "---------------------------------------" + "\n"
                    return res
                diff_f.append(fuel - self.output_fuel[pid])
            
            if diff_f[0] == 0 and diff_f.count(diff_f[0]) == len(diff_f):
                res += "Fuel Correct\n"
            else:
                res += "Fuel Incorrect\n"
                res += "plane\toutput\trate\tnormal\t\n"
                for pid, norm in enumerate(self.expected_fuel):
                    pid += 1
                    res += str(pid)+"\t"+str(self.output_fuel[pid])+"\t"+str(rate)+"\t"+str(norm)+"\n"

            diff_n = list()
            diff_a = list()
            for pid, (norm, alt) in enumerate(
                    zip(self.expected_land["normal"], self.expected_land["alternate"])):
                pid += 1
                diff_n.append( norm - self.output_land[pid])
                diff_a.append( alt  - self.output_land[pid])
            
            if diff_n[0] == 0 and diff_n.count(diff_n[0]) == len(diff_n):
                res += "Land Normal Correct\n"
            elif diff_a[0] == 0 and diff_a.count(diff_a[0]) == len(diff_a):
                res += "Land Alternate Correct\n"
            else:
                res += "Land not matched\n"
                res += "plane\toutput\tnormal\talternate\n" 
                for pid, (norm, alt) in enumerate(
                        zip(self.expected_land["normal"], self.expected_land["alternate"])):
                    pid += 1
                    res += str(pid)+"\t"+str(self.output_land[pid])+"\t"+str(norm)+"\t"+str(alt) + "\n"
        res += "---------------------------------------" + "\n"
        return res


time_unit = int(sys.argv[1])
cases = 10 * [None]
# (pid, planes, wait_time, wait_after, total_time, possible, expected_fuel, expected_land_1, expected_land_2)


# simple
planes = [(1,10,1,1,1,2)]
wait_time = [0]
cases[0] = testCase(1, planes, wait_time, 0, 0, True, [8], [2], [1], time_unit)

# wait in air
planes = [(1,10,1,1,1,1), (2,10,1,1,1,1), (3,10,1,1,1,1)]
wait_time = [0,0,0]
cases[1] = testCase(2, planes, wait_time, 0, 0, True, [8,8,7], [2,2,3], [1,1,1], time_unit)

# no wait (long enough delay)
planes = [(1,10,1,1,1,4), (2,10,1,1,1,1), (3,10,1,1,1,1)]
wait_time = [0, 4.5, 4]
cases[2] = testCase(3, planes, wait_time, 0, 0, True, [8,8,8], [2,6,10], [1,5,9], time_unit)

# no wait (very close delay)
planes = [(1,10,1,1,1,4), (2,10,1,1,1,5), (3,10,1,1,3,2)]
wait_time = [0, 1.5, 1]
cases[3] = testCase(4, planes, wait_time, 0, 0, True, [8,8,6], [2,3,10], [1,2,3], time_unit)

# more in each time unit (beginning & end)
planes = [(1,10,1,1,1,1), (2,10,1,1,1,1)]
wait_time = [0, 0.5]
cases[4] = testCase(5, planes, wait_time, 0, 0, True, [8,8], [2,2], [1,1], time_unit)

# fuel fraction success
planes = [(1,10,3,1,1,4)]
wait_time = [0]
cases[5] = testCase(6, planes, wait_time, 0, 0, True, [4], [2], [1], time_unit)

# fuel fraction crash
planes = [(1,5,3,1,1,4), (2,10,1,1,1,1)]
wait_time = [0, 4.5]
cases[6] = testCase(7, planes, wait_time, 0, 0, False, [], None, None, time_unit)

# fuel 0
planes = [(1,0,1,1,1,4)]
wait_time = [0]
cases[7] = testCase(8, planes, wait_time, 0, 0, False, [], None, None, time_unit)

# parking crash
planes = [(1,41,2,1,5,3), (2,41,2,1,5,3), (3,90,1,1,1,10)]
wait_time = [0,0,0]
cases[8] = testCase(9, planes, wait_time, 0, 0, False, [], None, None, time_unit)

# crash in air
planes = [(1,10,1,1,1,1), (2,10,1,1,1,1), (3,1,1,1,1,1)]
wait_time = []
cases[9] = testCase(10,planes, wait_time, 0, 0, False, [], None, None, time_unit)


f = open("result.txt", "w")
for case in cases:
#case = cases[0]
#print case
    case.execute()
    case.parse_output("output.txt")
    res = case.check_output()
    f.write(res)
    f.flush()

f.close()

