import { shallowMount, createLocalVue } from "@vue/test-utils";
import Vuex from "vuex";
import Toolbar from "@/components/Toolbar.vue";
import Dashboard from "@/views/Dashboard.vue";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import About from "@/views/About.vue";
import LogonButton from "@/components/LogonButton.vue";

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
    const text = "new message";
    const wrapper = shallowMount(Header, {
      propsData: { text }
    });
    expect(wrapper.text()).toMatch(text);
  });

  describe("Toolbar.vue", () => {
    it("renders Toolbar Component when passed", () => {
      const wrapper = shallowMount(Toolbar, { store, localVue });
      expect(wrapper.find(Toolbar)).toBeDefined();
      expect(getters.routes).toHaveBeenCalled();
    });
  });

  describe("Dashboard.vue", () => {
    it("renders Toolbar Component when passed", () => {
      const wrapper = shallowMount(Dashboard);
      expect(wrapper.find(Dashboard)).toBeDefined();
    });
  });

  describe("Footer.vue", () => {
    it("renders Toolbar Component when passed", () => {
      const wrapper = shallowMount(Footer);
      expect(wrapper.find(Footer)).toBeDefined();
    });
  });

  describe("LogonButton.vue", () => {
    it("renders Toolbar Component when passed", () => {
      const wrapper = shallowMount(LogonButton);
      expect(wrapper.find(LogonButton)).toBeDefined();
    });
  });

  describe("About.vue", () => {
    it("renders Toolbar Component when passed", () => {
      const wrapper = shallowMount(About);
      expect(wrapper.find(About)).toBeDefined();
    });
  });
});
