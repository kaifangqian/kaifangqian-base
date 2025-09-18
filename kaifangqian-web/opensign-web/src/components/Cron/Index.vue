<!--
  @description 开放签

  Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.

  注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
  必须公开修改后的完整源码（包括衍生作品），详见协议全文。
-->

<template>
  <div class="v3c">
    <ul class="v3c-tab">
      <li class="v3c-tab-item" :class="{ 'v3c-active': tabActive == 1 }" @click="onHandleTab(1)">秒</li>
      <li class="v3c-tab-item" :class="{ 'v3c-active': tabActive == 2 }" @click="onHandleTab(2)">分</li>
      <li class="v3c-tab-item" :class="{ 'v3c-active': tabActive == 3 }" @click="onHandleTab(3)">时</li>
      <li class="v3c-tab-item" :class="{ 'v3c-active': tabActive == 4 }" @click="onHandleTab(4)">日</li>
      <li class="v3c-tab-item" :class="{ 'v3c-active': tabActive == 5 }" @click="onHandleTab(5)">月</li>
      <li class="v3c-tab-item" :class="{ 'v3c-active': tabActive == 6 }" @click="onHandleTab(6)">年</li>
      <li class="v3c-tab-item" :class="{ 'v3c-active': tabActive == 7 }" @click="onHandleTab(7)">Cron表达式</li>
      <li class="v3c-tab-item" :class="{ 'v3c-active': tabActive == 8 }" @click="onHandleTab(8)">可定频率</li>
    </ul>
    <!-- 秒 -->
    <div class="v3c-content" v-show="tabActive == 1">
      <!-- 每一秒 -->
      <div>
        <label for="seconds1">
          <input type="radio" id="seconds1" value="1" v-model="state.second.cronEvery" />
          每秒
        </label>
      </div>
      <!-- 每隔多久 -->
      <div class="mt-20">
        <label for="seconds2">
          <input type="radio" id="seconds2" value="2" v-model="state.second.cronEvery" />
          每隔
          <a-input-number min="1" max="60" v-model:value="state.second.incrementIncrement" />
          秒执行 从
          <a-input-number  min="0" max="59" v-model:value="state.second.incrementStart" />
         秒开始
        </label>
      </div>
      <!-- 具体秒数 -->
      <div class="mt-20">
        <label for="seconds3">
          <input type="radio" id="seconds3" value="3" v-model="state.second.cronEvery" />
          具体秒数
          <select multiple v-model="state.second.specificSpecific">
            <option :value="index" v-for="(item, index) in 60" :key="index">{{ index }}</option>
          </select>
        </label>
      </div>
      <!-- 具体秒数 -->
      <div class="mt-20">
        <label for="seconds4">
          <input type="radio" id="seconds4" value="4" v-model="state.second.cronEvery" />
          周期从
          <input type="number" v-model="state.second.rangeStart" min="1" max="60" />
          到
          <input type="number" v-model="state.second.rangeEnd" min="0" max="59" />
          秒
        </label>
      </div>
    </div>
    <!-- 分钟 -->
    <div class="v3c-content" v-show="tabActive == 2">
      <!-- 每一秒 -->
      <div>
        <label for="minute1">
          <input type="radio" id="minute1" value="1" v-model="state.minute.cronEvery" />
          每一分钟
        </label>
      </div>
      <!-- 每隔多久 -->
      <div class="mt-20">
        <label for="minute2">
          <input type="radio" id="minute2" value="2" v-model="state.minute.cronEvery" />
          每隔
          <input type="number" min="1" max="60" v-model="state.minute.incrementIncrement" />
          分执行 从
          <input type="number" min="0" max="59" v-model="state.minute.incrementStart" />
          分开始
        </label>
      </div>
      <!-- 具体秒数 -->
      <div class="mt-20">
        <label for="minute3">
          <input type="radio" id="minute3" value="3" v-model="state.minute.cronEvery" />
          具体分钟数
          <select multiple v-model="state.minute.specificSpecific">
            <option :value="index" v-for="(item, index) in 60" :key="index">{{ index }}</option>
          </select>
        </label>
      </div>
      <!-- 具体秒数 -->
      <div class="mt-20">
        <label for="minute4">
          <input type="radio" id="minute4" value="4" v-model="state.minute.cronEvery" />
         周期从
          <input type="number" v-model="state.minute.rangeStart" min="1" max="60" />
         到
          <input type="number" v-model="state.minute.rangeEnd" min="0" max="59" />
         分
        </label>
      </div>
    </div>
    <!-- 小时 -->
    <div class="v3c-content" v-show="tabActive == 3">
      <!-- 每一秒 -->
      <div>
        <label for="hour1">
          <input type="radio" id="hour1" value="1" v-model="state.hour.cronEvery" />
          每一小时
        </label>
      </div>
      <!-- 每隔多久 -->
      <div class="mt-20">
        <label for="hour2">
          <input type="radio" id="hour2" value="2" v-model="state.hour.cronEvery" />
          每隔
          <input type="number" min="1" max="60" v-model="state.hour.incrementIncrement" />
          小时执行 从
          <input type="number" min="0" max="59" v-model="state.hour.incrementStart" />
          小时开始
        </label>
      </div>
      <!-- 具体秒数 -->
      <div class="mt-20">
        <label for="hour3">
          <input type="radio" id="hour3" value="3" v-model="state.hour.cronEvery" />
          具体分钟数
          <select multiple v-model="state.hour.specificSpecific">
            <option :value="index" v-for="(item, index) in 60" :key="index">{{ index }}</option>
          </select>
        </label>
      </div>
      <!-- 具体秒数 -->
      <div class="mt-20">
        <label for="hour4">
          <input type="radio" id="hour4" value="4" v-model="state.hour.cronEvery" />
          周期从
          <input type="number" v-model="state.hour.rangeStart" min="1" max="60" />
          到
          <input type="number" v-model="state.hour.rangeEnd" min="0" max="59" />
          小时
        </label>
      </div>
    </div>
    <!-- 天 -->
    <div class="v3c-content" v-show="tabActive == 4">
      <!-- 1 -->
      <div>
        <label for="day1">
          <input type="radio" id="day1" value="1" v-model="state.day.cronEvery" />
          每一天
        </label>
      </div>
      <!-- 2 -->
      <div class="mt-20">
        <label for="day2">
          <input type="radio" id="day2" value="2" v-model="state.day.cronEvery" />
          每隔
          <input type="number" min="1" max="60" v-model="state.day.incrementIncrement" />
          周执行 从
          <input type="number" min="0" max="59" v-model="state.day.incrementStart" />
          开始
        </label>
      </div>
      <!-- 3 -->
      <div class="mt-20">
        <label for="day3">
          <input type="radio" id="day3" value="3" v-model="state.day.cronEvery" />
          每隔
          <input type="number" v-model="state.hour.rangeStart" min="1" max="30" />
          天执行 从
          <input type="number" v-model="state.hour.rangeEnd" min="1" max="30" />
          天开始
        </label>
      </div>
      <!-- 4 -->
      <div class="mt-20">
        <label for="day4">
          <input type="radio" id="day4" value="4" v-model="state.day.cronEvery" />
          具体星期几
          <select multiple v-model="state.week.specificSpecific">
            <option v-for="(val, index) in 7" :key="index" :value="['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'][val - 1]">
              {{ state.text.Week[val - 1] }}
            </option>
          </select>
        </label>
      </div>
      <!-- 5 -->
      <div class="mt-20">
        <label for="day5">
          <input type="radio" id="day5" value="5" v-model="state.day.cronEvery" />
          具体天数
          <select multiple v-model="state.week.specificSpecific">
            <option v-for="(val, index) in 31" :key="index" :value="val">
              {{ val }}
            </option>
          </select>
        </label>
      </div>
      <!-- 6 -->
      <div class="mt-20">
        <label for="day6">
          <input type="radio" id="day6" value="6" v-model="state.day.cronEvery" />
          在这个月的最后一天
        </label>
      </div>
      <!-- 7 -->
      <div class="mt-20">
        <label for="day7">
          <input type="radio" id="day7" value="7" v-model="state.day.cronEvery" />
          在这个月的最后一个工作日
        </label>
      </div>
      <!-- 8 -->
      <div class="mt-20">
        <label for="day8">
          <input type="radio" id="day8" value="8" v-model="state.day.cronEvery" />
          在这个月的最后一个
          <select v-model="state.day.cronLastSpecificDomDay">
            <option v-for="(val, index) in 7" :key="index" :value="val">
              {{ state.text.Week[val - 1] }}
            </option>
          </select>
          {{ state.text.Day.lastWeek[1] || "" }}
        </label>
      </div>
      <!-- 9 -->
      <div class="mt-20">
        <label for="day9">
          <input type="radio" id="day9" value="9" v-model="state.day.cronEvery" />

          <input type="number" v-model="state.day.cronDaysBeforeEomMinus" min="1" max="31" />
              在本月底前
        </label>
      </div>
      <!-- 10 -->
      <div class="mt-20">
        <label for="day10">
          <input type="radio" id="day10" value="10" v-model="state.day.cronEvery" />
          {{ state.text.Day.nearestWeekday[0] }}
          <input type="number" v-model="state.day.cronDaysNearestWeekday" :min="1" :max="31" />
          {{ state.text.Day.nearestWeekday[1] }}
        </label>
      </div>
      <!-- 11 -->
      <div class="mt-20">
        <label for="day11">
          <input type="radio" id="day11" value="11" v-model="state.day.cronEvery" />
          {{ state.text.Day.someWeekday[0] }}
          <input type="number" v-model="state.week.cronNthDayNth" :min="1" :max="5" />
          &nbsp;
          <select v-model="state.week.cronNthDayDay">
            <option v-for="(val, index) in 7" :key="index" :value="val">
              {{ state.text.Week[val - 1] }}
            </option>
          </select>
          {{ state.text.Day.someWeekday[1] }}
        </label>
      </div>
    </div>
    <!-- 月 -->
    <div class="v3c-content" v-show="tabActive == 5">
      <!-- 1 -->
      <div>
        <label for="month1">
          <input type="radio" id="month1" value="1" v-model="state.month.cronEvery" />
          {{ state.text.Month.every }}
        </label>
      </div>
      <!-- 2 -->
      <div class="mt-20">
        <label for="month2">
          <input type="radio" id="month2" value="2" v-model="state.month.cronEvery" />
          {{ state.text.Month.interval[0] }}
          <input type="number" v-model="state.month.incrementIncrement" :min="0" :max="12" />
          {{ state.text.Month.interval[1] }}
          <input type="number" v-model="state.month.incrementStart" :min="0" :max="12" />
        </label>
      </div>
      <!-- 3 -->
      <div class="mt-20">
        <label for="month3">
          <input type="radio" id="month3" value="3" v-model="state.month.cronEvery" />
          {{ state.text.Month.specific }}
          <select multiple v-model="state.month.specificSpecific">
            <option v-for="(val, index) in 12" :key="index" :value="val">
              {{ val }}
            </option>
          </select>
        </label>
      </div>
      <!-- 4 -->
      <div class="mt-20">
        <label for="month4">
          <input type="radio" id="month4" value="4" v-model="state.month.cronEvery" />
          {{ state.text.Month.cycle[0] }}
          <input type="number" v-model="state.month.rangeStart" :min="1" :max="12" />
          {{ state.text.Month.cycle[1] }}
          <input type="number" v-model="state.month.rangeEnd" :min="1" :max="12" />
        </label>
      </div>
    </div>
    <!-- 年 -->
    <div class="v3c-content" v-show="tabActive == 6">
      <!-- 1 -->
      <div>
        <label for="year1">
          <input type="radio" id="year1" value="1" v-model="state.year.cronEvery" />
          {{ state.text.Year.every }}
        </label>
      </div>
      <!-- 2 -->
      <div class="mt-20">
        <label for="year2">
          <input type="radio" id="year2" value="2" v-model="state.year.cronEvery" />
          {{ state.text.Year.interval[0] }}
          <input type="number" v-model="state.year.incrementIncrement" :min="1" :max="99" />
          {{ state.text.Year.interval[1] }}
          <input type="number" v-model="state.year.incrementStart" :min="currYear" :max="currYear + 10" />
        </label>
      </div>
      <!-- 3 -->
      <div class="mt-20">
        <label for="year3">
          <input type="radio" id="year3" value="3" v-model="state.year.cronEvery" />
          {{ state.text.Year.specific }}
          <select multiple v-model="state.year.specificSpecific">
            <option v-for="(val, index) in 100" :key="index" :value="currYear + val">
              {{ currYear + val }}
            </option>
          </select>
        </label>
      </div>
      <!-- 4 -->
      <div class="mt-20">
        <label for="year3">
          <input type="radio" id="year3" value="4" v-model="state.year.cronEvery" />
          {{ state.text.Year.cycle[0] }}
          <input type="number" v-model="state.month.rangeStart" :min="currYear" :max="currYear + 10" />
          {{ state.text.Year.cycle[1] }}
          <input type="number" v-model="state.month.rangeEnd" :min="currYear" :max="currYear + 10" />
        </label>
      </div>
    </div>

    <div class="v3c-content" v-show="tabActive == 7">
       <div>
        <label for="seconds1">
          重复次数
          <a-select multiple v-model="state.second.repeatCount" style="width:150px"> 
            <a-select-option :value="item.value" v-for="(item, index) in state.countList" >{{ item.label }}</a-select-option>
          </a-select>
          *
          <a-input-number v-model:value="state.month.rangeEnd" :min="1" :max="12" placeholder="请输入次数"/>
        </label>
        <label for="seconds1">
          执行间隔
          <a-input-number type="number" v-model:value="state.month.rangeEnd" :min="1" :max="12" placeholder="请输入执行间隔" />
        </label>
      </div>
    </div> 
     <div class="v3c-content" v-show="tabActive == 8">
      <!-- 1 -->
      <div>
        <label for="year1">
          <input type="radio" id="year1" value="1" v-model="state.year.cronEvery" />
          单次
        </label>
      </div>
      <!-- 2 -->
      <div class="mt-20">
        <label for="year2">
          <input type="radio" id="year2" value="2" v-model="state.year.cronEvery" />
          无限次   执行间隔
          <input type="number" v-model="state.year.incrementIncrement" :min="1" :max="99" /> 秒
        </label>
      </div>
      <!-- 3 -->
      <div class="mt-20">
        <label for="year3">
          <input type="radio" id="year3" value="3" v-model="state.year.cronEvery" /> 自定义次数
          执行次数 <input type="number" v-model="state.year.incrementIncrement" :min="1" :max="99" /> 次
          执行间隔 <input type="number" v-model="state.year.incrementIncrement" :min="1" :max="99" /> 次
        
        </label>
      </div>
    </div>
    <!-- 结果 -->
    <div class="v3c-footer"  v-show="(tabActive != 7) && (tabActive != 8) ">
      <div style="flex: 1; margin-left:20px;">
        CRON &nbsp;: &nbsp;&nbsp;<a-input v-model:value="state.cron" style="width:200px"/>
        <!-- CRON &nbsp;: &nbsp;&nbsp;<span class="cron">{{ state.cron }}</span>
        &nbsp; &nbsp; &nbsp; -->
        <!-- <button class="btn-ok" @click.stop="handleChange">{{ state.text.Save }}</button> -->
      </div>
    </div>
  </div>
</template>

<script>
import Language from "./language";
import { reactive, computed, toRefs, defineComponent, ref, watch } from "vue";

export default defineComponent({
  name: "Vue3CronCore",
  props: {
    i18n: {},
    maxHeight: String,
    change: Function,
    value: String,
  },
  setup(props, { emit }) {
    const { i18n } = toRefs(props);
    const state = reactive({
      language: i18n.value,
      second: {
        cronEvery: "1",
        incrementStart: 3,
        incrementIncrement: 5,
        rangeStart: 0,
        rangeEnd: 0,
        specificSpecific: [],
      },
      minute: {
        cronEvery: "1",
        incrementStart: 3,
        incrementIncrement: 5,
        rangeStart: 0,
        rangeEnd: 0,
        specificSpecific: [],
      },
      hour: {
        cronEvery: "1",
        incrementStart: 3,
        incrementIncrement: 5,
        rangeStart: 0,
        rangeEnd: 0,
        specificSpecific: [],
      },
      day: {
        cronEvery: "1",
        incrementStart: 1,
        incrementIncrement: 1,
        rangeStart: 0,
        rangeEnd: 0,
        specificSpecific: [],
        cronLastSpecificDomDay: 1,
        cronDaysBeforeEomMinus: 0,
        cronDaysNearestWeekday: 1,
      },
      week: {
        cronEvery: "1",
        incrementStart: 1,
        incrementIncrement: 1,
        specificSpecific: [],
        cronNthDayDay: 1,
        cronNthDayNth: 1,
      },
      month: {
        cronEvery: "1",
        incrementStart: 3,
        incrementIncrement: 5,
        rangeStart: 1,
        rangeEnd: 1,
        specificSpecific: [],
      },
      year: {
        cronEvery: "1",
        incrementStart: 2022,
        incrementIncrement: 1,
        rangeStart: 1,
        rangeEnd: 1,
        specificSpecific: [],
      },
      output: {
        second: "",
        minute: "",
        hour: "",
        day: "",
        month: "",
        Week: "",
        year: "",
      },
      countList:[{
        label:'自定义',
        value:'custom'
      }],
      text: computed(() => Language[state.language || "cn"]),
      secondsText: computed(() => {
        let seconds = "";
        let cronEvery = state.second.cronEvery;
        switch (cronEvery.toString()) {
          case "1":
            seconds = "*";
            break;
          case "2":
            seconds = state.second.incrementStart + "/" + state.second.incrementIncrement;
            break;
          case "3":
            state.second.specificSpecific.map((val) => {
              seconds += val + ",";
            });
            seconds = seconds.slice(0, -1);
            break;
          case "4":
            seconds = state.second.rangeStart + "-" + state.second.rangeEnd;
            break;
        }
        return seconds;
      }),
      minutesText: computed(() => {
        let minutes = "";
        let cronEvery = state.minute.cronEvery;
        switch (cronEvery.toString()) {
          case "1":
            minutes = "*";
            break;
          case "2":
            minutes = state.minute.incrementStart + "/" + state.minute.incrementIncrement;
            break;
          case "3":
            state.minute.specificSpecific.map((val) => {
              minutes += val + ",";
            });
            minutes = minutes.slice(0, -1);
            break;
          case "4":
            minutes = state.minute.rangeStart + "-" + state.minute.rangeEnd;
            break;
        }
        return minutes;
      }),
      hoursText: computed(() => {
        let hours = "";
        let cronEvery = state.hour.cronEvery;
        switch (cronEvery.toString()) {
          case "1":
            hours = "*";
            break;
          case "2":
            hours = state.hour.incrementStart + "/" + state.hour.incrementIncrement;
            break;
          case "3":
            state.hour.specificSpecific.map((val) => {
              hours += val + ",";
            });
            hours = hours.slice(0, -1);
            break;
          case "4":
            hours = state.hour.rangeStart + "-" + state.hour.rangeEnd;
            break;
        }
        return hours;
      }),
      daysText: computed(() => {
        let days = "";
        let cronEvery = state.day.cronEvery;
        switch (cronEvery.toString()) {
          case "1":
            break;
          case "2":
          case "4":
          case "11":
            days = "?";
            break;
          case "3":
            days = state.day.incrementStart + "/" + state.day.incrementIncrement;
            break;
          case "5":
            state.day.specificSpecific.map((val) => {
              days += val + ",";
            });
            days = days.slice(0, -1);
            break;
          case "6":
            days = "L";
            break;
          case "7":
            days = "LW";
            break;
          case "8":
            days = state.day.cronLastSpecificDomDay + "L";
            break;
          case "9":
            days = "L-" + state.day.cronDaysBeforeEomMinus;
            break;
          case "10":
            days = state.day.cronDaysNearestWeekday + "W";
            break;
        }
        return days;
      }),
      weeksText: computed(() => {
        let weeks = "";
        let cronEvery = state.day.cronEvery;
        switch (cronEvery.toString()) {
          case "1":
          case "3":
          case "5":
            weeks = "?";
            break;
          case "2":
            weeks = state.week.incrementStart + "/" + state.week.incrementIncrement;
            break;
          case "4":
            state.week.specificSpecific.map((val) => {
              weeks += val + ",";
            });
            weeks = weeks.slice(0, -1);
            break;
          case "6":
          case "7":
          case "8":
          case "9":
          case "10":
            weeks = "?";
            break;
          case "11":
            weeks = state.week.cronNthDayDay + "#" + state.week.cronNthDayNth;
            break;
        }
        return weeks;
      }),
      monthsText: computed(() => {
        let months = "";
        let cronEvery = state.month.cronEvery;
        switch (cronEvery.toString()) {
          case "1":
            months = "*";
            break;
          case "2":
            months = state.month.incrementStart + "/" + state.month.incrementIncrement;
            break;
          case "3":
            state.month.specificSpecific.map((val) => {
              months += val + ",";
            });
            months = months.slice(0, -1);
            break;
          case "4":
            months = state.month.rangeStart + "-" + state.month.rangeEnd;
            break;
        }
        return months;
      }),
      yearsText: computed(() => {
        let years = "";
        let cronEvery = state.year.cronEvery;
        switch (cronEvery.toString()) {
          case "1":
            years = "*";
            break;
          case "2":
            years = state.year.incrementStart + "/" + state.year.incrementIncrement;
            break;
          case "3":
            state.year.specificSpecific.map((val) => {
              years += val + ",";
            });
            years = years.slice(0, -1);
            break;
          case "4":
            years = state.year.rangeStart + "-" + state.year.rangeEnd;
            break;
        }
        return years;
      }),
      cron: computed(() => {
        return `${state.secondsText || "*"} ${state.minutesText || "*"} ${state.hoursText || "*"} ${state.daysText || "*"} ${state.monthsText || "*"} ${state.weeksText || "?"} ${
          state.yearsText || "*"
        }`;
      }),
    });

    const handleChange = () => {
      if (typeof state.cron !== "string") return false;
      emit("change", state.cron);
    };
    const rest = (data) => {
      for (let i in data) {
        if (data[i] instanceof Object) {
          this.rest(data[i]);
        } else {
          switch (typeof data[i]) {
            case "object":
              data[i] = [];
              break;
            case "string":
              data[i] = "";
              break;
          }
        }
      }
    };

    const tabActive = ref(1);
    const currYear = new Date().getFullYear() - 1;
    const onHandleTab = (index) => {
      tabActive.value = index;
    };

    watch(
      () => state.cron,
      (value) => {
        if (typeof state.cron !== "string") return;
        emit("update:value", value);
      }
    );

    return {
      state,
      handleChange,
      rest,
      tabActive,
      onHandleTab,
      currYear,
    };
  },
});
</script>

<style lang="css" scoped>
.v3c {
  width: auto;
  border: 1px solid #f5f7fa;
}
.v3c-tab {
    padding: 0;
    list-style: none;
    margin: 0;
    display: flex;
    border-bottom: 1px solid #f5f7fa;
}

.v3c-tab-item {
  /* flex: 1; */
  text-align: center;
  cursor: pointer;
  padding: 10px;
  width: 100px;
}

.v3c-tab-item.v3c-active {
  color: #127fd2;
  border-bottom: 2px solid #127fd2;
}

.v3c-lang-btn {
  background-color: #127fd2;
  color: #ffffff;
  /* border-radius: 10px; */
}

.v3c-content {
  padding: 20px;
  max-height: v-bind(maxHeight);
  overflow: hidden;
  overflow-y: auto;
}

.p-20 {
  padding: 20px;
}

.v3c-footer {
  /* background-color: #f5f7fa; */
  padding-top: 10px;
  padding-bottom: 10px;
  display: flex;
  text-align: left;
}

.mt-20 {
  margin-top: 20px;
}

.v3c input[type="text"] {
  width: 80px;
  padding: 0 5px;
}

.v3c input[type="number"] {
  width: 80px;
  height: 28px;
  border: 1px solid #d9d9d9;
  padding: 0 5px;
}

.v3c select {
  width: 80px;
  height: 30px;
  border: 1px solid #d9d9d9;
}

.v3c select[multiple] {
  width: 80px;
  height: 100px;
  border: 1px solid #d9d9d9;
}

.btn-ok {
  line-height: 1.5715;
  position: relative;
  display: inline-block;
  font-weight: 400;
  white-space: nowrap;
  text-align: center;
  background-image: none;
  border: 1px solid transparent;
  box-shadow: 0 2px #00000004;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  touch-action: manipulation;
  height: 32px;
  padding: 4px 15px;
  font-size: 14px;
  border-radius: 2px;

  color: #fff;
  background: #5b8ff9;
  border-color: #5b8ff9;
  text-shadow: 0 -1px 0 rgb(0 0 0 / 12%);
  box-shadow: 0 2px #0000000b;
}

.btn-close {
  line-height: 1.5715;
  position: relative;
  display: inline-block;
  font-weight: 400;
  white-space: nowrap;
  text-align: center;
  background-image: none;
  border: 1px solid transparent;
  box-shadow: 0 2px #00000004;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  touch-action: manipulation;
  height: 32px;
  padding: 4px 15px;
  font-size: 14px;
  border-radius: 2px;

  color: #fff;
  background: #61ddaa;
  border-color: #61ddaa;
  text-shadow: 0 -1px 0 rgb(0 0 0 / 12%);
  box-shadow: 0 2px #0000000b;
}

.cron {
  background-color: #61ddaa;
  padding: 5px;
  padding-left: 10px;
  padding-right: 10px;
  color: #ffffff;
}
</style>
