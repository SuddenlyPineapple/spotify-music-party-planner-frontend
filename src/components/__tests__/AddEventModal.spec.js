import {
    createLocalVue,
    shallowMount
} from "@vue/test-utils";
import AddEventModal from "../AddEventModal";
import axios from 'axios'
import {
    apiUrl
} from "../../config/backend";

jest.mock('../../config/backend', () => ({
    apiUrl: 'http://test.pl/'
}))
jest.mock('axios', () => ({
    post: jest.fn(() => Promise.resolve())
}))

const localVue = createLocalVue()
const getWrapper = (propsData) => shallowMount(AddEventModal, {
    localVue,
    propsData
})

describe("AddEventModal component", () => {
    it("should save event and send request", () => {
        const userId = "TestUser123";
        const wrapper = getWrapper({
            userId
        });
        const data = {
            date: '123',
            time: 'some date',
            event: {
                name: "Test event",
                location: "Aveue Park",
                open: true
            },
        }
        wrapper.setData(data)
        wrapper.vm.saveEvent();
        expect(axios.post).toBeCalledWith('http://test.pl/events', {
            date: data.date + "T" + data.time + ":00Z",
            hostId: "TestUser123",
            location: "Aveue Park",
            name: "Test event",
            open: true,
        })
    });
})
