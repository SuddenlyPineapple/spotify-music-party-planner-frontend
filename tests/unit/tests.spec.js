import { shallowMount, createLocalVue } from "@vue/test-utils";
import Vuex from "vuex";
import HelloWorld from "@/components/HelloWorld.vue";
import Toolbar from "@/components/Toolbar.vue";

const localVue = createLocalVue();

localVue.use(Vuex);

describe("Spotify Party Music Planner Tests", () => {
  let getters;
  let store;

  beforeEach(() => {
    getters = {
      routes: jest.fn()
    };
    store = new Vuex.Store({
      getters
    });
  });

  it("renders HelloWorld Component when passed", () => {
    const msg = "new message";
    const wrapper = shallowMount(HelloWorld, {
      propsData: { msg }
    });
    expect(wrapper.text()).toMatch(msg);
  });

  describe("Toolbar.vue", () => {
    it("renders Toolbar Component when passed", () => {
      const wrapper = shallowMount(Toolbar, { store, localVue });
      expect(wrapper.find(Toolbar)).toBeDefined();
      expect(getters.routes).toHaveBeenCalled();
    });
  });
});
