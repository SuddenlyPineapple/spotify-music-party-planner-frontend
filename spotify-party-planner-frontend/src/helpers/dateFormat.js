import moment from "moment";

export default dateString =>
  moment.utc(dateString).format("D MMM YYYY, h:mm A");
