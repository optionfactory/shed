<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<table>
  <tr>
    <td colspan="2">
      <tiles:insertAttribute name="header" />
    </td>
  </tr>
  <tr>
    <td>
      ${body}
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <tiles:insertAttribute name="footer" />
    </td>
  </tr>
</table>