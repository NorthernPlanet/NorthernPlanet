<template>
  <div class="d-flex flex-column justify-content-center align-items-center">
    <div class="button-setting">
      <!-- location -->
      <div
        class="border-setting"
        @keyup.up="selectTop"
        @keyup.right="selectRight"
        @keyup.left="selectLeft"
        @keyup.49="selectSize(0)"
        @keyup.50="selectSize(1)"
        @keyup.51="selectSize(2)"
        @keyup.52="selectSize(3)"
        @keyup.53="selectSize(4)"
      >
        <h2 class="text-center">location</h2>
      </div>
      <div class="template-container">
        <img
          src="@/assets/presentationTemplates/presentation-right.jpg"
          alt=""
          :class="[
            { 'insert-border': presentationLocation === 'right' },
            'template-insert',
            'img-fluid',
          ]"
        />
        <div @click="selectRight" class="overlay">
          <span v-if="presentationLocation === 'right'">selected</span>
          <span v-else>Presentation On Right</span>
        </div>
      </div>
      <div class="template-container">
        <img
          src="@/assets/presentationTemplates/presentation-left.jpg"
          alt=""
          :class="[
            { 'insert-border': presentationLocation === 'left' },
            'template-insert',
            'img-fluid',
          ]"
        />
        <div @click="selectLeft" class="overlay">
          <span v-if="presentationLocation === 'left'">selected</span>
          <span v-else>Presentation On Left</span>
        </div>
      </div>
      <div class="template-container">
        <img
          src="@/assets/presentationTemplates/presentation-top.jpg"
          alt=""
          :class="[
            { 'insert-border': presentationLocation === 'top' },
            'template-insert',
            'img-fluid',
          ]"
        />
        <div @click="selectTop" class="overlay">
          <span v-if="presentationLocation === 'top'">selected</span>
          <span v-else>Presentation On Top</span>
        </div>
      </div>
      <!-- size -->
      <div class="size-controller">
        <h2 class="text-center">size</h2>
        <input
          type="range"
          class="range-select size-class"
          min="0"
          max="4"
          step="1"
          v-model="selectedSize"
        />
      </div>
    </div>
  </div>
</template>

<script>
// import "./template.scss";

export default {
  name: 'PresentationLocationSelector',
  components: {},
  // : props
  props: {},
  // : data
  data() {
    return {
      selectedSize: null,
      selectedLocation: null,
    };
  },
  // : computed
  computed: {
    slideUrls() {
      return this.$store.state.meetingRoom.imageSrcs;
    },
    transition() {
      return this.$store.state.meetingRoom.transition;
    },
    currentPage() {
      return this.$store.state.meetingRoom.currentPage;
    },
    presentationSize() {
      return this.$store.state.meetingRoom.size;
    },
    presentationLocation() {
      return this.$store.state.meetingRoom.location;
    },
    presentationTransition() {
      return this.$store.state.meetingRoom.transition;
    },
    LocationLeft() {
      return this.$store.state.meetingRoom.left;
    },
    LocationTop() {
      return this.$store.state.meetingRoom.top;
    },
    LocationRight() {
      return this.$store.state.meetingRoom.right;
    },
    size0() {
      return this.$store.state.meetingRoom.size0;
    },
    size1() {
      return this.$store.state.meetingRoom.size1;
    },
    size2() {
      return this.$store.state.meetingRoom.size2;
    },
    size3() {
      return this.$store.state.meetingRoom.size3;
    },
    size4() {
      return this.$store.state.meetingRoom.size4;
    },
  },
  // : watch
  watch: {
    now: function () {
      // 발표자의 현재 이미지 url state에 저장: 이미지 변경 시 -> actions / mutation으로 분리해야함
      this.$store.state.meetingRoom.currentPage = this.now;
      // 현재 본인이 발표자라면 웹소켓 메시지 보내기
      if (
        this.$store.state.meetingRoom.presenter ===
        this.$store.state.meetingRoom.myName
      ) {
        var message = {
          id: 'changePresentation',
          currentPage: this.now,
          location: this.presentationLocation,
          size: this.presentationSize,
          transition: this.transition,
        };
        this.$store.dispatch('meetingRoom/sendMessage', message);
      }
    },
    selectedSize: function () {
      const message = {
        id: 'changePresentation',
        currentPage: this.currentPage,
        location: this.presentationLocation,
        size: this.selectedSize,
        transition: this.presentationTransition,
      };
      this.$store.dispatch('meetingRoom/sendMessage', message);
    },
    LocationLeft: function () {
      this.selectLeft();
    },
    LocationTop: function () {
      this.selectTop();
    },
    LocationRight: function () {
      this.selectRight();
    },
    size0: function () {
      this.selectSize(0);
    },
    size1: function () {
      this.selectSize(1);
    },
    size2: function () {
      this.selectSize(2);
    },
    size3: function () {
      this.selectSize(3);
    },
    size4: function () {
      this.selectSize(4);
    },
  },
  // : lifecycle hook
  mounted() {
    // 매번 들어 올때, location, size를 state에 설정된 값으로 설정
    this.selectedSize = this.presentationSize;
    this.selectedLocation = this.presentationLocation;
  },
  // : methods
  methods: {
    selectSize: function (size) {
      console.log('this.selectedSize: ' + this.selectedSize);
      this.selectedSize = size;
    },
    selectRight: function () {
      this.selectedLocation = 'right';
      const message = {
        id: 'changePresentation',
        currentPage: this.currentPage,
        location: this.selectedLocation,
        size: this.presentationSize,
        transition: this.presentationTransition,
      };
      this.$store.dispatch('meetingRoom/sendMessage', message);
    },
    selectLeft: function () {
      this.selectedLocation = 'left';
      const message = {
        id: 'changePresentation',
        currentPage: this.currentPage,
        location: this.selectedLocation,
        size: this.presentationSize,
        transition: this.presentationTransition,
      };
      this.$store.dispatch('meetingRoom/sendMessage', message);
    },
    selectTop: function () {
      this.selectedLocation = 'top';
      const message = {
        id: 'changePresentation',
        currentPage: this.currentPage,
        location: this.selectedLocation,
        size: this.presentationSize,
        transition: this.presentationTransition,
      };
      this.$store.dispatch('meetingRoom/sendMessage', message);
    },
  },
};
</script>

<style scoped>
.button-setting {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.border-setting {
  border: 0px;
}
.template-container {
  position: relative;
  width: 240px;
  height: 180px;
  margin-bottom: 20px;
  cursor: pointer;
}
.template-insert {
  border-radius: 25px;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  width: 240px;
  height: 180px;
}
.insert-border {
  border: 0.4rem solid #495c4d;
}
.overlay {
  position: absolute;
  bottom: 0;
  border-radius: 25px;
  background: rgba(255, 255, 255, 0.8);
  width: 240px;
  height: 180px;
  transition: 0.3s ease;
  opacity: 0;
  color: black;
  font-size: 1.2rem;
  font-weight: bold;
  display: flex;
  justify-content: center;
  align-items: center;
}
.template-container:hover .overlay {
  opacity: 1;
}
.size-controller {
  margin-top: 0px;
  margin-bottom: 60px;
}
.range-select {
  -webkit-appearance: none;
  appearance: none;
  width: 240px;
  height: 15px;
  border-radius: 5px;
  background: #d3d3d3;
  outline: none;
  opacity: 0.7;
  -webkit-transition: 0.2s;
  transition: opacity 0.2s;
}
.range-select:hover {
  opacity: 1;
}
.range-select::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background: #15182a;
  cursor: pointer;
}
.range-select::-moz-range-thumb {
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background: #15182a;
  cursor: pointer;
}
.size-class {
  color: #505753;
  background: #aebdb6;
}
</style>
